package com.greenelegentfarmer.controller;

import javax.mail.MessagingException;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.greenelegentfarmer.entity.User;
import com.greenelegentfarmer.exception.InvalidIDException;
import com.greenelegentfarmer.model.NameModel;
import com.greenelegentfarmer.model.SubAdminModel;
import com.greenelegentfarmer.param.UserSearchParam;
import com.greenelegentfarmer.service.FileService;
import com.greenelegentfarmer.service.UserService;
import com.greenelegentfarmer.util.CrudController;
import com.greenelegentfarmer.util.ResponseModel;

@RestController
@RequestMapping("/user")
public class UserController extends CrudController<User> {
	
	private UserService userService;
	public UserController(UserService service) {
		super(service);
		userService=service;
	}

	@Autowired
	private FileService storageService;
	

	@GetMapping("/who/am/i")
	public ResponseEntity<ResponseModel> whoAmI() {
		
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("resource.fetched"))
				.setData("user", userService.whoAmI())
				.build(), HttpStatus.OK);
	}
	
	@PostMapping(value = "/image")
	public ResponseEntity<ResponseModel> image(@RequestPart MultipartFile image) throws Exception {
		
		User user=userService.whoAmI();
		user.setImagePath(storageService.save(image,"profile/"+user.getId()));
		
		userService.add(user);
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("resource.updated"))
				.setData("user", user)
				.build(),HttpStatus.OK);
	}
	
	@PutMapping(value = "/profile")
	public ResponseEntity<ResponseModel> update(@RequestBody NameModel name) throws Exception {
		
		User user=userService.whoAmI();
		user.setFirstName(name.getFirstName());
		user.setLastName(name.getLastName());
		userService.add(user);
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("resource.created"))
				.setData("user", user)
				.build(),HttpStatus.OK);
	}
	
	@PutMapping("/toggle-active/{id}")
	public ResponseEntity<ResponseModel> toggleActive(@PathVariable Long id) throws InvalidIDException {
		User user=service.expect(id);
		user.setEnabled(!user.isEnabled());
		service.add(user);
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("resource.created"))
				.build(), HttpStatus.OK);
	}
	
	@PostMapping("/filter")
	public ResponseEntity<ResponseModel> filter(@RequestBody UserSearchParam param) {
		Page<User> result=userService.search(param);
		
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("resource.fetched"))
				.setData("list", result.getContent())
				.setData("total", result.getTotalElements())
				.build(), HttpStatus.OK);
	}
	
	@PostMapping("/add/subadmin")
	public ResponseEntity<ResponseModel> filter(@RequestBody SubAdminModel subAdmin) throws MessagingException {
		User user = userService.addSubAdmin(subAdmin);
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("resource.created"))
				.setData("user", user)
				.build(), HttpStatus.OK);
		}
}
