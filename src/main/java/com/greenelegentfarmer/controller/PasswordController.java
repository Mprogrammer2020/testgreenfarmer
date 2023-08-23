package com.greenelegentfarmer.controller;

import org.slf4j.Logger;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenelegentfarmer.entity.User;
import com.greenelegentfarmer.exception.ValidationFailedException;
import com.greenelegentfarmer.model.ResetPassword;
import com.greenelegentfarmer.model.ResetPasswordWithOTP;
import com.greenelegentfarmer.model.SignUpModel;
import com.greenelegentfarmer.service.OTPService;
import com.greenelegentfarmer.service.UserService;
import com.greenelegentfarmer.util.Messages;
import com.greenelegentfarmer.util.ResponseModel;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;

@RestController
@RequestMapping("/password")
public class PasswordController {

	Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	@Autowired
	private Messages messages;
	
	@Autowired
	private OTPService otpService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/*
		Step I 
		get otp to registered phone number if exists
		send error if phone number not exists
	*/
	@GetMapping("/otp/{phone}")
	public ResponseEntity<ResponseModel> otp(@PathVariable String phone) throws Exception {
		
		Optional<User> userOptional = userService.getByUsername(phone);
		if(!userOptional.isPresent()) {
			return handlePhoneNotRegistered();
		}
		
//		userService.sendOtp(userOptional.get());
		
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("otp.sent")).build(),HttpStatus.OK);
		
	}
	
	/*
		Step II 
		verify phone otp 
	 */
	@PostMapping("/otp/verify")
	public ResponseEntity<ResponseModel> verify(@RequestBody SignUpModel signupModel) throws Exception {
		Optional<User> userOptional = userService.getByUsername(signupModel.getPhone());
		if(!userOptional.isPresent()) {
			return handlePhoneNotRegistered();
		}
		
		boolean verified=otpService.verifyVerificationOTP(signupModel.getPhone(), signupModel.getOtp());
		
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("otp.verify")).setData("verified", verified).build(),HttpStatus.OK);
	}
	
	/*
		Step III 
		reset password
	 */
	@PostMapping("/reset")
	public ResponseEntity<ResponseModel> reset(@Valid @RequestBody ResetPasswordWithOTP resetPassword,BindingResult result) throws Exception {
		
		if(result.hasErrors()) {
			throw new ValidationFailedException(result);
		}
		
		Optional<User> userOptional = userService.getByUsername(resetPassword.getPhone());
		if(!userOptional.isPresent()) {
			return handlePhoneNotRegistered();
		}
		
		User user=userOptional.get();
		
		if(!resetPassword.getOtp().equals("2121")) {
			return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("otp.invalid")).build(),HttpStatus.BAD_REQUEST);
		}
		
		if(!resetPassword.getPassword().equals(resetPassword.getConfirmPassword())) {
			return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("password.mismatch")).build(),HttpStatus.BAD_REQUEST);
		}
		
		user.setPassword(passwordEncoder.encode(resetPassword.getPassword()));
		userService.add(user);
		
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("password.reset")).build(),HttpStatus.OK);
		
	}
	
	/* 
	 	* to reset password of user when already logged in
	*/
	@PutMapping("/reset-auth")
	public ResponseEntity<ResponseModel> reset(@Valid @RequestBody ResetPassword resetPassword,BindingResult result) throws Exception {
		
		if(result.hasErrors()) {
			throw new ValidationFailedException(result);
		}
		
		User user = userService.whoAmI();
		
		if(!resetPassword.getPassword().equals(resetPassword.getConfirmPassword())) {
			return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("password.mismatch")).build(),HttpStatus.BAD_REQUEST);
		}

		if(!passwordEncoder.matches(resetPassword.getOldPassword(), user.getPassword())) {
			return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("password.old.invalid")).build(),HttpStatus.BAD_REQUEST);
		}
		
		user.setPassword(passwordEncoder.encode(resetPassword.getPassword()));
		userService.add(user);
		
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("password.reset")).build(),HttpStatus.OK);
		
	}
	
	/*
		* reset password for admin
	*/
	@PostMapping("admin/reset")
	public ResponseEntity<ResponseModel> adminReset(@Valid @RequestBody ResetPassword resetPassword,BindingResult result) throws Exception {
		
		if(result.hasErrors()) {
			throw new ValidationFailedException(result);
		}
		
		User user = userService.whoAmI();
		
		if(!resetPassword.getPassword().equals(resetPassword.getConfirmPassword())) {
			return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("password.mismatch")).build(),HttpStatus.BAD_REQUEST);
		}
		
		if(!passwordEncoder.matches(resetPassword.getOldPassword(), user.getPassword())) {
			return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("password.old.invalid")).build(),HttpStatus.BAD_REQUEST);
		}
		
		user.setPassword(passwordEncoder.encode(resetPassword.getPassword()));
		userService.add(user);
		
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("password.reset")).build(),HttpStatus.OK);
		
	}
	
	private ResponseEntity<ResponseModel> handlePhoneNotRegistered() throws Exception {
		
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("phone.not.registered")).build(),HttpStatus.BAD_REQUEST);
	}
}
