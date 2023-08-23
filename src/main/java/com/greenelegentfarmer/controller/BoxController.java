package com.greenelegentfarmer.controller;

import javax.validation.Valid;
import java.util.stream.Collectors;
import com.greenelegentfarmer.entity.Box;
import org.springframework.http.HttpStatus;
import com.greenelegentfarmer.model.BoxItem;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import com.greenelegentfarmer.util.ResponseModel;
import com.greenelegentfarmer.service.BoxService;
import com.greenelegentfarmer.service.FileService;
import com.greenelegentfarmer.service.ItemService;
import com.greenelegentfarmer.util.CrudController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/box")
public class BoxController extends CrudController<Box>{

	public BoxController(BoxService service) {
		super(service);
	}

	@Autowired
	private ItemService itemService;
	@Autowired
	private FileService storageService;
	
	
	@Override
	protected void validateResource(Box body, BindingResult result) throws Exception {
		if(body.getId()!=null) {
			Box savedBox=service.expect(body.getId());
			
			body.setImagePath(savedBox.getImagePath());
			body.setItems(savedBox.getItems());
		}
		super.validateResource(body, result);
	}
	
	@PutMapping("/image")
	public ResponseEntity<ResponseModel> image(@RequestParam MultipartFile image,@RequestParam Long id) throws Exception {
		Box box=service.expect(id);
		
		box.setImagePath(storageService.save(image,"item/"+id));
		
		service.add(box);
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("resource.created"))
				.build(), HttpStatus.OK);
	}
	
	@PutMapping("/assign")
	public ResponseEntity<ResponseModel> assign(@Valid @RequestBody BoxItem boxItem) throws Exception {
		Box box=service.expect(boxItem.getBoxId());
		box.setItems(boxItem.getItems().stream().map(id -> itemService.getById(id).get()).collect(Collectors.toList()));
		
		service.add(box);
		return new ResponseEntity<ResponseModel>(new ResponseModel.ResponseModelBuilder(messages.get("resource.created"))
				.build(), HttpStatus.OK);
	}

}
