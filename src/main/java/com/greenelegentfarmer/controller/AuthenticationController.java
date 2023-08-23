package com.greenelegentfarmer.controller;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenelegentfarmer.entity.User;
import com.greenelegentfarmer.model.LoginModel;
import com.greenelegentfarmer.service.OTPService;
import com.greenelegentfarmer.service.UserService;
import com.greenelegentfarmer.util.JwtTokenUtil;
import com.greenelegentfarmer.util.Messages;
import com.greenelegentfarmer.util.ResponseModel;

@RestController
public class AuthenticationController {
	Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	@Autowired
	private Messages messages;
	
	@Autowired
	private OTPService otpService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@RequestMapping("/")
	public String firstPage() {
		return "Hello Green Elegent Farmer! ";
	}
	
	@PostMapping("/login")
	public ResponseEntity<ResponseModel> login(@RequestBody LoginModel user) throws Exception {
		if(user.getPassword()==null) 
			return handleOTPLogin(user);
		else 
			return handlePasswordLogin(user);
	}
	
	/*
		If phone not exists then not registered
		If phone already exists and otp not in request then send otp
		If phone already exists and otp in request then validate otp
	*/
	private ResponseEntity<ResponseModel> handleOTPLogin(LoginModel user) throws Exception {
		
		User userSaved=null;
		Optional<User> userOptional = userService.getByUsername(user.getPhone());
		if(!userOptional.isPresent()) {
			return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("phone.not.registered")).build(),HttpStatus.BAD_REQUEST);
		}
		
		userSaved=userOptional.get();

		if(user.getOtp()==null) {
			otpService.sendLoginOTP(user.getPhone());
			return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("otp.sent")).build(),HttpStatus.OK);
		}
		else if(!user.getOtp().equals("2121")) {
			return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("otp.invalid")).build(),HttpStatus.BAD_REQUEST);
		}
		
		//authentication done with OTP passing userSaved instead of user
		return onSuccessLogin(userSaved);
	}
	
	/*
		if phone not found then phone not registered
		if password not match after encoding then invalid
	*/
	private ResponseEntity<ResponseModel> handlePasswordLogin(LoginModel user) throws Exception {
		
		String username = user.getPhone() != null ? user.getPhone() : user.getEmail();
		Optional<User> userOptional = userService.getByUsername(username);
		if(!userOptional.isPresent()) {
			return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(user.getPhone() != null ? messages.get("phone.not.registered") : messages.get("email.not.registered"))
					.build(),HttpStatus.BAD_REQUEST);
		}
		User userSaved=userOptional.get();

		if(userSaved.isEnabled()!=false)//this throw exception but we need custom message in response
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, user.getPassword()));
		
		return onSuccessLogin(userSaved);
	}
	
	private ResponseEntity<ResponseModel> onSuccessLogin(User user) throws Exception {
		
		if(user.isEnabled() == false) {
			return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("user.suspended")).build(),HttpStatus.FORBIDDEN);
		}
			
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder("Token created successfully!")
				.setData("token", jwtTokenUtil.generateToken(user))
				.setData("user", user)
				.build(),HttpStatus.CREATED);
	}
}
