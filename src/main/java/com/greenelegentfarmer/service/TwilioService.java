package com.greenelegentfarmer.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioService {

	public static final String ACCOUNT_SID = "ACdc7ebfde92cf09915a9609cc8f9ac9c6";
	public static final String AUTH_TOKEN = "473d2553c89130a861221f14371d707e";
	public static final String FROM="+12765826741";
	
    public static void main(String[] args) {
    	 Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
         Message message = Message.creator(
        		 new PhoneNumber("+918219345971"),
        		 new PhoneNumber(FROM),
                 "Hi there sakshi")
             .create();

         System.out.println(message.getSid());
    }
}
