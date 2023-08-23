package com.greenelegentfarmer.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.greenelegentfarmer.entity.Item;
import com.greenelegentfarmer.entity.UserSubscription;
import com.greenelegentfarmer.exception.InvalidIDException;
import com.greenelegentfarmer.service.ItemService;
import com.greenelegentfarmer.service.UserService;
import com.greenelegentfarmer.service.UserSubscriptionService;
import com.greenelegentfarmer.util.Messages;
import com.greenelegentfarmer.util.ResponseModel;

@RestController
@RequestMapping("/subscription")
public class UserSubscriptionController {

	@Autowired
	private Messages messages;
	@Autowired
	private ItemService itemService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserSubscriptionService userSubscriptionService;
	
	@GetMapping
	public ResponseEntity<ResponseModel> mySubscription(@RequestParam(required = false) Long user_id) throws Exception {
		if(user_id==null) {
			user_id=userService.whoAmI().getId();
		}
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("resource.created"))
				.setData("resource", userSubscriptionService.myActiveSubscription(user_id))
				.build(), HttpStatus.OK);
	}
	
	@PostMapping("/pause")
	public ResponseEntity<ResponseModel> pauseSubscription() throws InvalidIDException {
		UserSubscription subscription=userSubscriptionService.pauseSubscription();
		
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("resource.updated"))
				.setData("resource", subscription)
				.build(), HttpStatus.OK);
	}
	
	@PostMapping("/customize")
	public ResponseEntity<ResponseModel> customizeSubscription(@RequestBody Long itemsIds[]) throws InvalidIDException {
		
		List<Item> items=new ArrayList<Item>();
		for(Long itemId:itemsIds) {
			Item item=itemService.expect(itemId);
			items.add(item);
		}
		
		UserSubscription subscription=userSubscriptionService.customize(items);
		
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("resource.updated"))
				.setData("resource", subscription)
				.build(), HttpStatus.OK);
	}
	
}
