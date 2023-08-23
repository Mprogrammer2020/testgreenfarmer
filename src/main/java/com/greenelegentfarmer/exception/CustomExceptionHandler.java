package com.greenelegentfarmer.exception;


import java.nio.file.FileAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import com.greenelegentfarmer.util.Messages;
import com.greenelegentfarmer.util.ResponseModel;
import com.stripe.exception.CardException;

@ControllerAdvice
public class CustomExceptionHandler {
	
	@Autowired
	private Messages message;
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception exception, WebRequest webRequest) {
		exception.printStackTrace();
		
		ResponseModel response = new ResponseModel.ResponseModelBuilder(exception.getLocalizedMessage()).build();
        ResponseEntity<Object> entity = new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        return entity;
    }
	
	@ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleException(BadCredentialsException exception, WebRequest webRequest) {
		exception.printStackTrace();
		
		ResponseModel response = new ResponseModel.ResponseModelBuilder("The credentials are not valid!").build();
        ResponseEntity<Object> entity = new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        return entity;
    }
	
	@ExceptionHandler(ValidationFailedException.class)
    public ResponseEntity<?> handleValidationFailedException(ValidationFailedException exception, WebRequest webRequest) {
		exception.printStackTrace();
		
		ResponseModel response = new ResponseModel.ResponseModelBuilder(message.get("validation.failed")).setErrors(exception.getResult()).build();
        ResponseEntity<Object> entity = new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        return entity;
    }
	
	@ExceptionHandler(InvalidIDException.class)
    public ResponseEntity<?> handleInvalidIDException(InvalidIDException exception, WebRequest webRequest) {
		exception.printStackTrace();
		
		ResponseModel response = new ResponseModel.ResponseModelBuilder(message.get("id.not.exists")).build();
        ResponseEntity<Object> entity = new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        return entity;
    }
	
	@ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(DataIntegrityViolationException exception, WebRequest webRequest) {
		exception.printStackTrace();
		
		ResponseModel response = new ResponseModel.ResponseModelBuilder(message.get("constraint.violation")).build();
        ResponseEntity<Object> entity = new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        return entity;
    }
	
	@ExceptionHandler(FileAlreadyExistsException.class)
    public ResponseEntity<?> handleFileAlreadyExistsException(FileAlreadyExistsException exception, WebRequest webRequest) {
		exception.printStackTrace();
		
		ResponseModel response = new ResponseModel.ResponseModelBuilder(message.get("resource.exists")+" "+exception.getMessage()).build();
        ResponseEntity<Object> entity = new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        return entity;
    }
	
	@ExceptionHandler(CardException.class)
    public ResponseEntity<?> handleStripeCardException(CardException exception) {
		exception.printStackTrace();
		
		String message[]=exception.getMessage().split(";");
		
		ResponseModel response = new ResponseModel.ResponseModelBuilder(message.length > 0 ? message[0] : "").build();
        ResponseEntity<Object> entity = new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        return entity;
    }
	
}
