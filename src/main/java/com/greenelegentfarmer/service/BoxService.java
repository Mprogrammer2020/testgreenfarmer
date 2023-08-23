package com.greenelegentfarmer.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.greenelegentfarmer.entity.Box;
import com.greenelegentfarmer.entity.Item;
import com.greenelegentfarmer.repository.BoxRepository;
import com.greenelegentfarmer.util.CrudService;


@Service
public class BoxService extends CrudService<Box> {

	@Autowired
	private ItemService itemService;
	
	public BoxService(BoxRepository repository) {
		super(repository);
	}
	
	@Override
	public Box add(Box box) {
		
		if(box.getItems()!=null && !box.getItems().isEmpty()) {
			List<Item> items=new ArrayList<Item>();
			box.getItems().forEach(item -> itemService.getById(item.getId()).ifPresent(it -> items.add(it)));
			box.setItems(items);
		}
		
		return super.add(box);
	}

}
