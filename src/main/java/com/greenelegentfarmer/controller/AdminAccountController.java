package com.greenelegentfarmer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenelegentfarmer.constant.Constants;
import com.greenelegentfarmer.entity.User;
import com.greenelegentfarmer.model.AdminModel;
import com.greenelegentfarmer.service.ItemService;
import com.greenelegentfarmer.service.UserOrderService;
import com.greenelegentfarmer.service.UserOrderTransactionService;
import com.greenelegentfarmer.service.UserService;
import com.greenelegentfarmer.util.Messages;
import com.greenelegentfarmer.util.ResponseModel;


@RestController
@Secured({"ROLE_ADMIN","ROLE_SUBADMIN"})
@RequestMapping("/admin/account")
public class AdminAccountController {

	@Autowired
	private Messages messages;
							
	@Autowired
	private UserService userService;
	
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private UserOrderService userOrderService;
	
	@Autowired
	private UserOrderTransactionService userOrderTransactionService;
	
	
	@GetMapping
	public ResponseEntity<ResponseModel> dashboard() {
		
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("resource.fetched"))
				.setData("users", userService.countByRole())
				.setData("items", itemService.countActiveItems())
				.setData("orders", userOrderService.countByStatus(Constants.INITIATED))
				.setData("sales", userOrderTransactionService.sale())
				.build(), HttpStatus.OK);
	}
	
	@PutMapping(value = "/profile")
	public ResponseEntity<ResponseModel> update(@RequestBody AdminModel adminModel) throws Exception {
		
		User user=userService.whoAmI();
		user.setPhone(adminModel.getPhone());
		user.setFirstName(adminModel.getFirstName());
		user.setLastName(adminModel.getLastName());
		userService.add(user);
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("resource.created"))
				.setData("user", user)
				.build(),HttpStatus.OK);
	}
	
	
	
	
}
