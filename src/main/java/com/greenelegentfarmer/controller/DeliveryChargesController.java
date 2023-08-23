package com.greenelegentfarmer.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import com.greenelegentfarmer.util.ResponseModel;
import com.greenelegentfarmer.util.CrudController;
import org.springframework.validation.BindingResult;
import com.greenelegentfarmer.entity.DeliveryCharges;
import com.greenelegentfarmer.service.DeliveryChargesService;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("delivery-charges")
public class DeliveryChargesController extends CrudController<DeliveryCharges> {

	private DeliveryChargesService service;
	public DeliveryChargesController(DeliveryChargesService service) {
		super(service);
		this.service=service;
	}
	
	@Override
	public ResponseEntity<ResponseModel> add(@Valid @RequestBody DeliveryCharges body, BindingResult result) throws Exception {
		List<DeliveryCharges> list=service.getAll();
		if(list.size()==1) body.setId(list.get(0).getId());
		return super.add(body, result);
	}
}
