package com.greenelegentfarmer.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.greenelegentfarmer.constant.Constants;
import com.greenelegentfarmer.entity.User;
import com.greenelegentfarmer.entity.UserOrder;
import com.greenelegentfarmer.entity.UserSubscription;
import com.greenelegentfarmer.param.UserOrderSearchParam;
import com.greenelegentfarmer.service.StripeCardService;
import com.greenelegentfarmer.service.UserOrderService;
import com.greenelegentfarmer.service.UserService;
import com.greenelegentfarmer.service.UserSubscriptionService;
import com.greenelegentfarmer.util.CrudController;
import com.greenelegentfarmer.util.ResponseModel;
import com.stripe.model.Charge;

@RestController
@RequestMapping("/user-order")
public class UserOrderController extends CrudController<UserOrder> {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private StripeCardService stripeCardService;
	
	@Autowired
	private UserSubscriptionService userSubscriptionService;
	
	private UserOrderService userOrderService;
	public UserOrderController(UserOrderService userOrderService) {
		super(userOrderService);
		this.userOrderService = userOrderService;
	}

	@GetMapping(value = "/active/{id}")
	public ResponseEntity<ResponseModel> activeOrders(@PathVariable Long id) throws Exception {
		User user=userService.expect(id);
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("resource.updated")).setData("orders", userOrderService.myActiveOrder(user)).build(),HttpStatus.OK);
	}
	
	
	@PutMapping(value = "/deliver/{id}")
	public ResponseEntity<ResponseModel> update(@PathVariable("id") Long id) throws Exception {
		UserOrder userOrder = userOrderService.expect(id);
		
		
		if(Constants.DELIVERED.equals(userOrder.getStatus())) {
			return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("order.delivered")).build(),HttpStatus.BAD_REQUEST);
		} else if(Constants.PAUSED.equals(userOrder.getStatus())) {
			return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("subscription.paused")).build(),HttpStatus.BAD_REQUEST);
		} else if(LocalDateTime.now().toLocalDate().isBefore(userOrder.getDeliveryDate().toLocalDate())) {
			return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("order.delivery.early")).build(),HttpStatus.BAD_REQUEST);
		}
		
		chargeAndMarkNext(userOrder);
		userOrder.setStatus(Constants.DELIVERED);
		userOrder.setDeliveryDate(LocalDateTime.now());
		userOrderService.add(userOrder);
		
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("resource.updated")).setData("order", userOrder).build(),HttpStatus.OK);
	}
	
	@PostMapping(value="/pay/{id}")
	public ResponseEntity<ResponseModel> pay(@PathVariable Long id) throws Exception {
		UserOrder userOrder = userOrderService.expect(id);
		
		if(Constants.SUCCESS.equals(userOrder.getPaymentStatus())) {
			return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("order.paid")).build(),HttpStatus.BAD_REQUEST);
		}
		
		chargeAndMarkNext(userOrder);
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("resource.updated")).setData("order", userOrder).build(),HttpStatus.OK);
	}
	
	private void chargeAndMarkNext(UserOrder userOrder) {
		Charge charge=stripeCardService.createCharge(userOrder,
				userOrder.getSubscription().getUser().getPrimaryCardId());
		
		if(charge==null) {
			userOrder.setPaymentStatus(Constants.FAILED);
			
			UserSubscription subscription=userOrder.getSubscription();
			subscription.setDelivery(null);
			subscription.setStatus(Constants.NOT_SCHEDULED);
			userSubscriptionService.add(subscription);
			
		} else {
			userOrder.setChargeId(charge.getId());
			userOrder.setPaymentStatus(Constants.SUCCESS);
			
			UserSubscription subscription=userOrder.getSubscription();
			
			if(!Constants.INACTIVE.equals(subscription.getStatus())) { //next delivery for active/paused/unscheduled subscription
				
				if(Constants.PAUSED.equals(subscription.getStatus())) {
					subscription.setDelivery(null);
				} else {
					UserOrder order=userOrderService.makeNextDelivery(userOrder);
					subscription.setDelivery(order.getDeliveryDate());
					subscription.setStatus(Constants.ACTIVE);
				}
				
				userSubscriptionService.add(subscription);
			}
		}
	}
	
	@PostMapping(value="filter")
	public ResponseEntity<ResponseModel> filter(@RequestBody UserOrderSearchParam search) throws Exception {
		Page<UserOrder> orders = userOrderService.filter(search); 
		
		List<UserOrder> list=orders.getContent().stream().map(order -> {
			order.getSubscription().setItems(null);
			order.getSubscription().getBox().setItems(null);
			order.getSubscription().getUser().setAddresses(null);
			return order;
		}).collect(Collectors.toList());
		
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("resource.fetched"))
				.setData("list", list)
				.setData("total", orders.getTotalElements())
				.build(),HttpStatus.OK);
	}	
}
