package com.greenelegentfarmer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenelegentfarmer.entity.User;
import com.greenelegentfarmer.service.StripeCardService;
import com.greenelegentfarmer.service.StripeService;
import com.greenelegentfarmer.service.UserService;
import com.greenelegentfarmer.util.ResponseModel;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentSource;

@RestController
@RequestMapping("/stripe")
public class StripeController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private StripeService stripeService;

	@Autowired
	private StripeCardService stripeCardService;

	@GetMapping("/all/customer")
	public ResponseEntity<ResponseModel> allCustomers() throws StripeException {

		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder("stripe customers")
			.setData("resource", stripeService.getCustomers()).build(), HttpStatus.OK);
	}

	@GetMapping("/customer/{customer_id}")
	public ResponseEntity<ResponseModel> customer(@PathVariable String customer_id) throws StripeException {
		Customer customer = stripeService.getCustomer(customer_id);

		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder("stripe customers")
				.setData("customer", customer)
				.build(), HttpStatus.OK);
	}

	@DeleteMapping("/customer/{customer_id}")
	public ResponseEntity<ResponseModel> delete(@PathVariable String customer_id) throws StripeException {

		stripeService.deleteCustomer(customer_id);

		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder("deleted").build(),
				HttpStatus.OK);
	}

	@PostMapping("/card/create/{token}")
	public ResponseEntity<ResponseModel> addCard(@PathVariable String token) throws StripeException {
		User user = userService.whoAmI();
		stripeCardService.addCard(user, token);

		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder("card added")
				.setData("customerId", user.getCustomerId()).setData("primaryCardId", user.getPrimaryCardId()).build(),
				HttpStatus.OK);

	}

	@GetMapping("/card/customer/{customer_id}")
	public ResponseEntity<ResponseModel> getCards(@PathVariable String customer_id) throws StripeException {
		List<PaymentSource> list = stripeCardService.getCards(customer_id);
		
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder("card fetched.").setData("resource", list).build(),HttpStatus.OK);
	}

	@GetMapping("/primary/card/{card_id}/customer/{customer_id}")
	public ResponseEntity<ResponseModel> makePrimary(@PathVariable String customer_id, @PathVariable String card_id) throws StripeException {
		stripeCardService.makePrimary(customer_id, card_id);

		User user = userService.whoAmI();
		user.setPrimaryCardId(card_id);
		userService.add(user);

		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder("primary card changed").build(), HttpStatus.OK);

	}

	@DeleteMapping("/card/{card_id}/customer/{customer_id}")
	public ResponseEntity<ResponseModel> deleteCard(@PathVariable String customer_id,@PathVariable String card_id) throws StripeException {
		stripeCardService.delete(customer_id, card_id);
		
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder("card deleted").build(), HttpStatus.OK);
	}
}
