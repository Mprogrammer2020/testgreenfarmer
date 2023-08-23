package com.greenelegentfarmer.util;

import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.greenelegentfarmer.dto.DTOToEntity;

/*
Written By - raul__manendez
An abstract utility for restcontroller
to easily get all CRUD operations to child
implementation. override the behavior if needed
args :
	 D : the DTO class for entity to perform crud
	 E : the entity class for CRUD
*/
public abstract class DTOCrudController<D extends DTOToEntity<E>, E> extends EntityRDController<E> {

	public DTOCrudController(CrudService<E> service) {
		super(service);
	}

	protected void validateResource(D body, BindingResult result) throws Exception {
		// override this to validate
	}

	protected E mapToEntity(D body) throws Exception {
		return body.toEntity();
	}

	@PostMapping
	public ResponseEntity<ResponseModel> add(@Valid @RequestBody D body, BindingResult result) throws Exception {
		this.validateResource(body, result);

		if (result.hasErrors()) {
			return new ResponseEntity<ResponseModel>(
					new ResponseModel.ResponseModelBuilder(messages.get("validation.failed")).setErrors(result).build(),
					HttpStatus.BAD_REQUEST);
		}

		E resource = service.add(mapToEntity(body));

		return new ResponseEntity<ResponseModel>(
				new ResponseModel.ResponseModelBuilder(messages.get("resource.created")).setData("resource", resource)
						.build(),
				HttpStatus.CREATED);
	}
}