package com.greenelegentfarmer.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.greenelegentfarmer.entity.User;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.stripe.net.RequestOptions;

@Service
public class StripeService {

	@Autowired
	private UserService userService;
	
	@PostConstruct
	private void initialize() {
		Stripe.apiKey = "sk_test_51MgmDnCKGTwQsUWLYecNFZS3VcxdpXcaj8RyaiYIkPFFX8WcheR7v9Ld0mUKzeGz22e445Ue2nAoxJtjYFvSEjml00DKGIhUU5";
		RequestOptions.builder().setApiKey(Stripe.apiKey).build();
	}

	public String createCustomer(User user) throws StripeException {

		Map<String, Object> params = new HashMap<>();
		params.put("email", null);
		params.put("name", user.getFirstName() + " " + user.getLastName());
		params.put("phone", user.getPhone());
		params.put("description", "");

		Customer customer = Customer.create(params);

		user.setCustomerId(customer.getId());
		userService.add(user);
		
		return user.getCustomerId();
	}

	public List<Customer> getCustomers() throws StripeException {

		Map<String, Object> params = new HashMap<>();
		params.put("limit", 3);

		CustomerCollection customers = Customer.list(params);

		return customers.getData();
	}

	public Customer getCustomer(String customer_id) throws StripeException {

		Customer customer = Customer.retrieve(customer_id);

		return customer;
	}

	public void deleteCustomer(String customer_id) throws StripeException {
		Customer customer = Customer.retrieve(customer_id);

		customer.delete();
	}
}
