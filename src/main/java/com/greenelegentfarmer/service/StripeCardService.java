package com.greenelegentfarmer.service;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import com.stripe.model.Card;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.PaymentSource;
import com.greenelegentfarmer.entity.Box;
import com.greenelegentfarmer.entity.User;
import com.greenelegentfarmer.entity.UserOrder;
import com.greenelegentfarmer.entity.UserOrderTransaction;
import com.stripe.exception.StripeException;
import org.springframework.stereotype.Service;
import com.stripe.model.PaymentSourceCollection;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class StripeCardService {

	@Autowired
	private UserService userService;

	@Autowired
	private StripeService stripeService;
	
	@Autowired
	private UserOrderTransactionService userOrderTransactionService;

	public void addCard(User user, String token) throws StripeException {

		if (user.getCustomerId() == null) {
			stripeService.createCustomer(user);
		}

		Map<String, Object> retrieveParams = new HashMap<>();
		List<String> expandList = new ArrayList<>();
		expandList.add("sources");
		retrieveParams.put("expand", expandList);
		Customer customer = Customer.retrieve(user.getCustomerId(), retrieveParams, null);

		Map<String, Object> params = new HashMap<>();
		params.put("source", token);// "tok_visa"

		Card card = (Card) customer.getSources().create(params);

		if (user.getPrimaryCardId() == null) {
			user.setPrimaryCardId(card.getId());
			userService.add(user);
		}
	}

	public List<PaymentSource> getCards(String customer_id) throws StripeException {
		if (customer_id == null)
			return new ArrayList<PaymentSource>();

		List<String> expandList = new ArrayList<>();
		expandList.add("sources");

		Map<String, Object> retrieveParams = new HashMap<>();
		retrieveParams.put("expand", expandList);

		Customer customer = Customer.retrieve(customer_id, retrieveParams, null);

		Map<String, Object> params = new HashMap<>();
		params.put("object", "card");
		// params.put("limit", 10); default limit is 10

		PaymentSourceCollection cards = customer.getSources().list(params);

		return cards.getData();
	}

	public Customer makePrimary(String customer_id, String card_id) throws StripeException {

		Customer customer = Customer.retrieve(customer_id);

		Map<String, Object> params = new HashMap<>();
		params.put("default_source", card_id);

		return customer.update(params);
	}

	public Card delete(String customer_id, String card_id) throws StripeException {
		Map<String, Object> retrieveParams = new HashMap<>();
		List<String> expandList = new ArrayList<>();
		expandList.add("sources");
		retrieveParams.put("expand", expandList);
		Customer customer = Customer.retrieve(customer_id, retrieveParams, null);

		Card card = (Card) customer.getSources().retrieve(card_id);

		return (Card) card.delete();
	}

	public Charge createCharge(UserOrder order,String source) {
		
		Box boxSubscribed=order.getSubscription().getBox();
		User subscribedBy=order.getSubscription().getUser();
		
		Map<String, Object> params = new HashMap<>();
		params.put("amount",  Math.round((order.getSubscriptionFee() + order.getDeliveryFee()) * 100));
		params.put("currency", "cad");
		
		if(source.equals(subscribedBy.getPrimaryCardId()))
			params.put("customer", subscribedBy.getCustomerId());
		
		params.put("source", source==null ? subscribedBy.getPrimaryCardId() : source);
		params.put("description", subscribedBy.getFirstName()+" "+subscribedBy.getLastName()+" | "+subscribedBy.getPhone()+" | "+boxSubscribed.getName());

		Charge charge=null;
		try {
			charge=Charge.create(params);
		} catch (StripeException e) {
			System.out.println("Error while creating charge :"+e.getMessage());
		}
		
		userOrderTransactionService.createTransaction(order, charge);
		
		return charge;
	}

}
