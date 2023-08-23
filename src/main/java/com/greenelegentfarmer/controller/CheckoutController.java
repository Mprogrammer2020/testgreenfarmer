package com.greenelegentfarmer.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.greenelegentfarmer.constant.Constants;
import com.greenelegentfarmer.dto.CheckoutDTO;
import com.greenelegentfarmer.entity.Box;
import com.greenelegentfarmer.entity.Item;
import com.greenelegentfarmer.entity.User;
import com.greenelegentfarmer.entity.UserAddress;
import com.greenelegentfarmer.entity.UserOrder;
import com.greenelegentfarmer.entity.UserOrderAddress;
import com.greenelegentfarmer.entity.UserSubscription;
import com.greenelegentfarmer.service.BoxService;
import com.greenelegentfarmer.service.DeliveryChargesService;
import com.greenelegentfarmer.service.DeliveryDayService;
import com.greenelegentfarmer.service.ItemService;
import com.greenelegentfarmer.service.UserAddressService;
import com.greenelegentfarmer.service.UserOrderService;
import com.greenelegentfarmer.service.UserService;
import com.greenelegentfarmer.service.UserSubscriptionService;
import com.greenelegentfarmer.util.Messages;
import com.greenelegentfarmer.util.ResponseModel;

@RestController
@RequestMapping("/checkout")
public final class CheckoutController {

	@Autowired
	private Messages messages;
	@Autowired
	private BoxService boxService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserOrderService userOrderService;
	@Autowired
	private UserAddressService userAddressService;
	@Autowired
	private DeliveryDayService deliveryDayService;
	@Autowired
	private DeliveryChargesService deliveryChargesService;
	@Autowired
	private UserSubscriptionService userSubscriptionService;
	
	
	@GetMapping
	public ResponseEntity<ResponseModel> data() throws Exception {
		
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("resource.created"))
					.setData("charges", deliveryChargesService.getAll())
					.setData("nextDeliveryDate", deliveryDayService.getNextDelivery()).build(), HttpStatus.CREATED);
	}
	
	
	@PostMapping
	public ResponseEntity<ResponseModel> add(@Valid @RequestBody CheckoutDTO checkout,BindingResult result) throws Exception {
		if(result.hasErrors()) {
			return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("validation.failed")).setErrors(result).build(),HttpStatus.BAD_REQUEST);
		}
		UserOrder order=checkout.toEntity();
		
		UserSubscription subscription=order.getSubscription();

		this.setSubscriptionData(checkout, subscription);
		this.setOrderData(order,checkout);
		
		
		userSubscriptionService.deactivePrevious();		
		userSubscriptionService.add(subscription);//add/update subscription
		userOrderService.add(order);//add new order to user's subscription
		
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("resource.created"))
				.build(), HttpStatus.CREATED);
	}
	
	private void setSubscriptionData(CheckoutDTO checkout,UserSubscription subscription) throws Exception {
		
		if(subscription.getId()!=null) {
			UserSubscription oldSubscription=userSubscriptionService.expect(subscription.getId());
			subscription.setItems(oldSubscription.getItems().stream().collect(Collectors.toList()));
		} else {
			List<Item> items = checkout.getItems().stream().map(itemId -> itemService.getById(itemId).get()).collect(Collectors.toList());
			subscription.setItems(items);
		}
		
		subscription.setStatus(Constants.ACTIVE);
		subscription.setDelivery(deliveryDayService.getNextDelivery());//needs to be changed
		
		Box boxSubscribed=boxService.expect(subscription.getBox().getId());
		User subscribedBy=userService.expect(userService.whoAmI().getId());
		
		subscription.setBox(boxSubscribed);
		subscription.setUser(subscribedBy);
	}
	
	private void setOrderData(UserOrder order,CheckoutDTO checkout) throws Exception {
		order.setStatus(Constants.INITIATED);
		order.setPaymentStatus(Constants.PENDING);
		order.setSourceId(checkout.getSource());
		
		UserAddress address;
		
		if(checkout.getAddressId()!=null) 
			address=userAddressService.expect(checkout.getAddressId());
		 else 
			address=checkout.getAddress();
		
		order.setAddress(new UserOrderAddress(address.getAddress(), address.getCity(), address.getState(), address.getZipCode()));
		
		order.setSubscriptionFee(order.getSubscription().getBox().getPrice());
		order.setDeliveryFee(deliveryChargesService.getDeliveryCharges());
		
		
		order.setDeliveryDate(order.getSubscription().getDelivery());
		order.setItems(order.getSubscription().getItems());
		
		
		order.getSubscription().setSubscriptionFee(order.getSubscriptionFee());
		order.getSubscription().setDeliveryFee(order.getDeliveryFee());
	}
}
