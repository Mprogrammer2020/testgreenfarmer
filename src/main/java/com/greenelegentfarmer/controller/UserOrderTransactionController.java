package com.greenelegentfarmer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.greenelegentfarmer.entity.User;
import com.greenelegentfarmer.entity.UserOrderTransaction;
import com.greenelegentfarmer.param.UserOrderTransactionParam;
import com.greenelegentfarmer.service.UserOrderTransactionService;
import com.greenelegentfarmer.service.UserService;
import com.greenelegentfarmer.util.Messages;
import com.greenelegentfarmer.util.ResponseModel;

@RestController
@RequestMapping("/transaction")
public class UserOrderTransactionController {

	@Autowired
	protected Messages messages;

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserOrderTransactionService userOrderTransactionService;
	
	
	@PostMapping(value="filter")
	public ResponseEntity<ResponseModel> getPendingRequests(@RequestBody UserOrderTransactionParam search) throws Exception {
		User me=userService.whoAmI();
		if(me.getRole().equals("ROLE_USER")) 
			search.setUser(me);
		
		
		Page<UserOrderTransaction> orders = userOrderTransactionService.filter(search); 
		
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("resource.fetched"))
				.setData("list", orders.getContent())
				.setData("total", orders.getTotalElements())
				.build(),HttpStatus.OK);
	}
	
}
