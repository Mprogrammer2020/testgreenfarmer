package com.greenelegentfarmer.model;

import javax.validation.constraints.NotBlank;

public class ResetPasswordWithOTP extends ResetPassword {

	@NotBlank
	private String phone;
	@NotBlank
	private String otp;
	 
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
	
}
