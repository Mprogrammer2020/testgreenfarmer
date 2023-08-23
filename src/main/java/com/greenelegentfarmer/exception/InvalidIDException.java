package com.greenelegentfarmer.exception;

public class InvalidIDException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public InvalidIDException() { }
	
	public InvalidIDException(String message){
		super(message);
	}

	
}
