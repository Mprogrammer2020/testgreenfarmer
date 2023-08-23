package com.greenelegentfarmer.util;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/*
	Written By - raul__manendez
	An abstract utility for messagesource
	to get required message from either 
	code | code + args | code + args + locale
*/
@Component
public final class Messages {

	@Autowired
	private MessageSource source;
	
	public String get(String code) {
		return source.getMessage(code, null, null);
	}
	
	public String get(String code,Object [] args) {
		return source.getMessage(code, args, null);
	}
	
	public String get(String code,Object [] args,Locale locale) {
		return source.getMessage(code, args, locale);
	}

}
