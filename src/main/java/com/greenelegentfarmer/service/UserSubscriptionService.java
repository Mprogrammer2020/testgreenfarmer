package com.greenelegentfarmer.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.greenelegentfarmer.util.CrudService;
import com.greenelegentfarmer.constant.Constants;
import com.greenelegentfarmer.entity.Item;
import com.greenelegentfarmer.entity.UserSubscription;
import com.greenelegentfarmer.repository.UserSubscriptionRepository;

@Service
public class UserSubscriptionService extends CrudService<UserSubscription> {

	private UserSubscriptionRepository repository;
	public UserSubscriptionService(UserSubscriptionRepository repository) {
		super(repository);
		this.repository=repository;
	}
	
	@Autowired
	private UserService userService;
	
	@Lazy
	@Autowired
	private UserOrderService userOrderService;
	
	public UserSubscription myActiveSubscription(Long id) {
		return repository.mySubscription(id);
	}
	
	public void deactivePrevious() {
		UserSubscription subscription=myActiveSubscription(userService.whoAmI().getId());
		
		if(subscription!=null) {
			subscription.setStatus(Constants.INACTIVE);
			add(subscription);	
			
			userOrderService.pauseActiveOrder(subscription,true);
		}
		
	}
	
	public UserSubscription pauseSubscription() {
		UserSubscription subscription=myActiveSubscription(userService.whoAmI().getId());
		
		if(subscription!=null) {
			subscription.setStatus(Constants.PAUSED);
			if(LocalDate.now().plusDays(2).isBefore(subscription.getDelivery().toLocalDate())) {
				subscription.setDelivery(null);
			} 
			add(subscription);	
		}
		
		userOrderService.pauseActiveOrder(subscription,false);
		
		return subscription;
	}
	
	public UserSubscription customize(List<Item> items) {
		UserSubscription subscription=myActiveSubscription(userService.whoAmI().getId());
		
		List<Long> oldItems=subscription.getItems().stream().map(item -> item.getId()).collect(Collectors.toList());
		List<Long> newItems=items.stream().map(item -> item.getId()).collect(Collectors.toList());
		
		subscription.setItems(items);
		add(subscription);
		
		userOrderService.myActiveOrder(subscription.getUser()).forEach(order -> {
			if(LocalDate.now().plusDays(2).isBefore(order.getDeliveryDate().toLocalDate())) {
				order.setItems(items);
				if(!oldItems.equals(newItems)) order.setCustomized(true);
				
				userOrderService.add(order);
			}
		});
		
		return subscription;
	}
}
