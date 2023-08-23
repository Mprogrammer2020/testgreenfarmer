package com.greenelegentfarmer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.greenelegentfarmer.entity.User;
import com.greenelegentfarmer.model.SignUpModel;
import com.greenelegentfarmer.service.OTPService;
import com.greenelegentfarmer.service.UserService;
import com.greenelegentfarmer.util.JwtTokenUtil;
import com.greenelegentfarmer.util.Messages;
import com.greenelegentfarmer.util.PasswordUtil;
import com.greenelegentfarmer.util.ResponseModel;

@RestController
@RequestMapping("/sign-up")
public class SignUpController {

	@Autowired
	private Messages messages;
	
	@Autowired
	private OTPService otpService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private PasswordUtil passwordUtil;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@GetMapping("/exists/{phone}")
	public ResponseEntity<ResponseModel> exists(@PathVariable String phone){
		boolean present=userService.getByUsername(phone).isPresent();
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("flag.present")).setData("present", present).build(),HttpStatus.OK);
	}
	
	//1
	@GetMapping("/otp/send/{phone}")
	public ResponseEntity<ResponseModel> sendOtp(@PathVariable String phone){
		otpService.sendVerificationOTP(phone);
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("otp.sent")).build(),HttpStatus.OK);
	}
	
	//2
	@PostMapping("/otp/verify")
	public ResponseEntity<ResponseModel> verify(@RequestBody SignUpModel signupModel){
		boolean verified=otpService.verifyVerificationOTP(signupModel.getPhone(), signupModel.getOtp());
		
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("otp.verify")).setData("verified", verified).build(),HttpStatus.OK);
	}
	
	//3
	@PostMapping
	public ResponseEntity<ResponseModel> signUp(@RequestBody SignUpModel signupModel) throws Exception {
		
		boolean present=userService.getByUsername(signupModel.getPhone()).isPresent();
		if(present) {
			return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("flag.present")).setData("present", present).build(),HttpStatus.OK);
		}
		
		if(signupModel.getPassword()==null && signupModel.getConfirmPassword()==null) {
			boolean verified=otpService.verifyVerificationOTP(signupModel.getPhone(), signupModel.getOtp());
			if(!verified) {
				return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("otp.verify")).setData("verified", verified).build(),HttpStatus.OK);
			}
			
			signupModel.setPassword(passwordUtil.generatePassayPassword());
			signupModel.setConfirmPassword(signupModel.getPassword());
			
		}
		
		User user=signupModel.toUser();
		
		user.setPassword(passwordEncoder.encode(signupModel.getPassword()));
		userService.register(user);
		
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("sign.up.continue"))
				.setData("token", jwtTokenUtil.generateToken(user))
				.setData("user", user)
				.build(),HttpStatus.OK);
	}
}
