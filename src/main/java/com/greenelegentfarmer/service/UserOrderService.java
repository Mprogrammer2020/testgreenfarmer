package com.greenelegentfarmer.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.greenelegentfarmer.constant.Constants;
import com.greenelegentfarmer.entity.Item;
import com.greenelegentfarmer.entity.User;
import com.greenelegentfarmer.entity.UserOrder;
import com.greenelegentfarmer.entity.UserOrderAddress;
import com.greenelegentfarmer.entity.UserSubscription;
import com.greenelegentfarmer.param.UserOrderSearchParam;
import com.greenelegentfarmer.util.CrudService;
import com.greenelegentfarmer.repository.UserOrderRepository;
import com.greenelegentfarmer.specification.UserOrderSpecification;

@Service
public class UserOrderService extends CrudService<UserOrder> implements UserOrderSpecification {

	@Autowired
	private DeliveryDayService deliveryDayService;
	
	@Autowired
	private DeliveryChargesService deliveryChargesService;
	
	private UserOrderRepository repository;
	public UserOrderService(UserOrderRepository repository) {
		super(repository);
		this.repository = repository;
	}
	
	public Long countByStatus(String status) {
		return repository.countByStatus(status);
	}
	
	public List<UserOrder> myActiveOrder(User user) {
		return repository.findBySubscriptionUserAndStatusOrderByIdDesc(user, Constants.INITIATED);
	}

	public Page<UserOrder> filter(UserOrderSearchParam param) {

		Sort sort = Sort.unsorted();
		
		if(Constants.INITIATED.equals(param.getStatus())) {
			sort = Sort.by("deliveryDate").ascending();
		} else if(Constants.DELIVERED.equals(param.getStatus())) {
			sort = Sort.by("deliveryDate").descending();
		} else {
			sort = Sort.by("orderDate").descending();
		}
		
		Pageable pageable = PageRequest.of(param.getPage(), param.getSize(), sort);

		Specification<UserOrder> specification = filterSpecification(param);

		return repository.findAll(specification, pageable);
	}
	
	public void pauseActiveOrder(UserSubscription subscription,boolean delete) {
		List<UserOrder> orders=repository.findBySubscriptionUserAndStatusOrderByIdDesc(subscription.getUser(), Constants.INITIATED);
		orders.addAll(repository.findBySubscriptionUserAndStatusOrderByIdDesc(subscription.getUser(), Constants.PAUSED));
		
		orders.stream().map(order -> {
			if(LocalDate.now().plusDays(2).isBefore(order.getDeliveryDate().toLocalDate())) {
				order.setStatus(delete ? Constants.DELETED : Constants.PAUSED);
			} 
			return order;
		}).collect(Collectors.toList());
		
		addAll(orders);
	}

	public UserOrder makeNextDelivery(UserOrder oldOrder) {
		// to add next delivery for user
		UserOrder nextUserOrder = new UserOrder();
		UserOrderAddress address = new UserOrderAddress();
			address.setAddress(oldOrder.getAddress().getAddress());
			address.setState(oldOrder.getAddress().getState());
			address.setCity(oldOrder.getAddress().getCity());
			address.setZipCode(oldOrder.getAddress().getZipCode());
			
		nextUserOrder.setAddress(address);
		nextUserOrder.setOrderDate(LocalDateTime.now());
		nextUserOrder.setStatus(Constants.INITIATED);
		nextUserOrder.setSubscriptionFee(oldOrder.getSubscription().getBox().getPrice());
		nextUserOrder.setDeliveryFee(deliveryChargesService.getDeliveryCharges());
		nextUserOrder.setSubscription(oldOrder.getSubscription());
		
		List<Item> newItems=oldOrder.getSubscription().getItems().stream()
				.filter(item -> item.getEnabled()==Boolean.TRUE)
				.collect(Collectors.toList());
		
		List<Item> oldItems=oldOrder.getItems().stream()
				.filter(item -> item.getEnabled()==Boolean.TRUE)
				.collect(Collectors.toList());
		
		if(!oldItems.equals(newItems)) {
			nextUserOrder.setCustomized(true);
		}
		
		
		nextUserOrder.setItems(newItems);
		LocalDateTime nextDeliveryDate=deliveryDayService.getNextDelivery(oldOrder.getSubscription().getFrequency());
		nextUserOrder.setDeliveryDate(nextDeliveryDate);
		
		return add(nextUserOrder);
	}

}
