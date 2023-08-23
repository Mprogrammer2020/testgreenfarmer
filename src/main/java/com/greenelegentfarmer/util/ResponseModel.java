package com.greenelegentfarmer.util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class ResponseModel {

	private String message;
	private LocalDateTime time;
	private Map<String, Object> data;
	private List<Map<String, Object>> errors;

	public String getMessage() {
		return message;
	}
	
	public LocalDateTime getTime() {
		return time;
	}

	public Map<String, Object> getData() {
		return data;
	}
	
	public List<Map<String, Object>> getErrors() {
		return errors;
	}

	private ResponseModel(ResponseModelBuilder builder) {
		this.data=builder.data;
		this.errors=builder.errors;
		this.message=builder.message;
		this.time=LocalDateTime.now();
	}
	
	public static class ResponseModelBuilder {
	
		private String message;
		private List<Map<String, Object>> errors;
		private Map<String, Object> data=new HashMap<String, Object>();

		public ResponseModelBuilder(String message) {
			this.message=message;
		}
		
		public ResponseModelBuilder setErrors(BindingResult result) {
			errors=new ArrayList<Map<String,Object>>(result.getErrorCount());
			
			for(FieldError fieldError : result.getFieldErrors()) {
				Map<String,Object> map=new HashMap<String, Object>(1);
				
				map.put("field", fieldError.getField());
				map.put("message", fieldError.getDefaultMessage());
				
				errors.add(map);
			}
			return this;
		}
		
		public ResponseModelBuilder setData(String key, Object value) {
			this.data.put(key, value);
			return this;
		}

		public ResponseModel build() {
			return new ResponseModel(this);
		}
	}
	
}
