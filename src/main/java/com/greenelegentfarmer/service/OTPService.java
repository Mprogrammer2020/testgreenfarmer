package com.greenelegentfarmer.service;

import org.springframework.stereotype.Service;
import com.greenelegentfarmer.entity.OTP;

@Service
public class OTPService {

	public void sendVerificationOTP(String phone) {
		OTP otp=new OTP();
		otp.setOtp("2121");
		otp.setPhone(phone);
		otp.setPurpose("VERIFICATION");
	}
	
	public void sendLoginOTP(String phone) {
		OTP otp=new OTP();
		otp.setOtp("2121");
		otp.setPhone(phone);
		otp.setPurpose("VERIFICATION");
	}
	
	public boolean verifyVerificationOTP(String phone,String OTP) {
		if(OTP.equals("2121"))
			return true;
		
		return false;
	}
	
}
