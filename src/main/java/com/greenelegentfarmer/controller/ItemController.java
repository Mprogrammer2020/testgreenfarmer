package com.greenelegentfarmer.controller;

import com.greenelegentfarmer.entity.Item;
import com.greenelegentfarmer.exception.InvalidIDException;
import com.greenelegentfarmer.param.ItemSearchParam;
import com.greenelegentfarmer.service.FileService;
import com.greenelegentfarmer.service.ItemService;
import com.greenelegentfarmer.util.CrudController;
import com.greenelegentfarmer.util.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/item")
public class ItemController extends CrudController<Item> {
	
	private ItemService service;
	public ItemController(ItemService service) {
		super(service);
		this.service=service;
	}
	
	@Autowired
	private FileService storageService;
	
	@Override
	protected void validateResource(Item body, BindingResult result) throws Exception {
		if(body.getId()!=null) {
			body.setImagePath(service.expect(body.getId()).getImagePath());
		}
		super.validateResource(body, result);
	}
	
	@PutMapping("/image")
	public ResponseEntity<ResponseModel> image(@RequestParam MultipartFile image,@RequestParam Long id) throws Exception {
		Item item=service.expect(id);
		
		item.setImagePath(storageService.save(image,"item/"+id));
		
		service.add(item);
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("resource.created"))
				.build(), HttpStatus.OK);
	}
	
	@PutMapping("/toggle-active/{id}")
	public ResponseEntity<ResponseModel> toggleActive(@PathVariable Long id) throws InvalidIDException {
		Item item=service.expect(id);
		item.setEnabled(!item.getEnabled());
		service.add(item);
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("resource.created"))
				.build(), HttpStatus.OK);
	}
	
	@PostMapping("/filter")
	public ResponseEntity<ResponseModel> filter(@RequestBody ItemSearchParam search) {
		Page<Item> pagedItem=service.search(search);
		
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("resource.fetched"))
				.setData("list", pagedItem.getContent())
				.setData("total", pagedItem.getTotalElements())
				.build(), HttpStatus.OK);
		
	}

}
