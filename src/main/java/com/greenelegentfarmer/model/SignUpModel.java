package com.greenelegentfarmer.model;

import com.greenelegentfarmer.entity.User;

public class SignUpModel extends NameModel {

	private Long id;
	private String countryCode;
	private String phone;
	private String otp;
	private String password;
	private String confirmPassword;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	public User toUser(){
		User user=new User();
		user.setCountryCode(countryCode);
		user.setPhone(phone);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		return user;
	}
}
