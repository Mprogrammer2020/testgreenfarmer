package com.greenelegentfarmer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import com.greenelegentfarmer.util.ResponseModel;
import com.greenelegentfarmer.entity.DeliveryDay;
import com.greenelegentfarmer.service.DeliveryDayService;
import com.greenelegentfarmer.util.CrudController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("delivery-day")
public class DeliveryDayController extends CrudController<DeliveryDay> {

	private DeliveryDayService service;
	public DeliveryDayController(DeliveryDayService service) {
		super(service);
		this.service=service;
	}
	
	@Override
	protected void validateResource(DeliveryDay body, BindingResult result) throws Exception {
		if(body.getId()==null || !service.getById(body.getId()).isPresent())
			result.rejectValue("id", null, "Please enter a valid id!");
		
		super.validateResource(body, result);
	}
	
	@Override
	public ResponseEntity<ResponseModel> getAll() {
		if(service.getAll().size()==0) 
			service.setInitialData();
		
		return super.getAll();
	}
}
