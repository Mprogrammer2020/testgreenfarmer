package com.greenelegentfarmer.exception;

import org.springframework.validation.BindingResult;

public class ValidationFailedException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private BindingResult result;
	
	public ValidationFailedException() { }

	public ValidationFailedException(BindingResult result){
		this.result=result;
	}

	public BindingResult getResult() {
		return result;
	}

}
