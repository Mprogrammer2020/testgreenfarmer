package com.greenelegentfarmer.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.greenelegentfarmer.entity.UserAddress;
import com.greenelegentfarmer.exception.InvalidIDException;
import com.greenelegentfarmer.service.UserAddressService;
import com.greenelegentfarmer.util.CrudController;
import com.greenelegentfarmer.util.ResponseModel;

@RestController
@RequestMapping("/address")
public class UserAddressController extends CrudController<UserAddress> {

	private UserAddressService service;
	public UserAddressController(UserAddressService service) {
		super(service);
		this.service=service;
	}
	
	@GetMapping("/user/my")
	public ResponseEntity<ResponseModel> get() {
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("resource.fetched"))
				.setData("list", service.myAddresses())
				.build(), HttpStatus.OK);
	}
	
	@Override
	protected void validateResource(UserAddress body, BindingResult result) throws Exception {
		if(result.hasErrors())
			return;
		
		UserAddress primaryAddress=service.primaryAddress();
		
		if(primaryAddress==null)
			body.setPrimaryA(true);
	}
	
	@PutMapping("/primary/{address_id}")
	public ResponseEntity<ResponseModel> switchPrimary(@PathVariable Long address_id) throws InvalidIDException {
		
		UserAddress newPrimary=service.expect(address_id);
		if(newPrimary.getPrimaryA()!=null && newPrimary.getPrimaryA()) {
			return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder("Already a primary address!")
					.setData("resource", newPrimary)
					.build(), HttpStatus.OK);
		}
		
		List<UserAddress> myAddresses=service.myAddresses();
		
		myAddresses.forEach(address -> {
			if(address.getId()==address_id)
				address.setPrimaryA(true);
			else
				address.setPrimaryA(false);
		});
		
		service.addAll(myAddresses);
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("resource.updated"))
				.setData("resource", newPrimary)
				.build(), HttpStatus.OK);
	}
	
	@Override
	@Secured({"ROLE_USER","ROLE_ADMIN"})
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseModel> delete(@PathVariable Long id) throws Exception {
		UserAddress primaryAddress=service.primaryAddress();
		
		if(primaryAddress.getId().equals(id)) {
			return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("constraint.not.allowed")).build(), HttpStatus.NOT_ACCEPTABLE);
		}
		return super.delete(id);
	}

}


