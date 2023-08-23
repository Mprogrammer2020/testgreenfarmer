package com.greenelegentfarmer.service;

import com.greenelegentfarmer.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.greenelegentfarmer.param.ItemSearchParam;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.greenelegentfarmer.util.CrudService;
import com.greenelegentfarmer.util.SpecificationFactory;
import com.greenelegentfarmer.repository.ItemRepository;

@Service
public class ItemService extends CrudService<Item> implements SpecificationFactory<Item> {

	private ItemRepository repository;
	public ItemService(ItemRepository repository) {
		super(repository);
		this.repository=repository;
	}
	
	public Long countActiveItems() {
		return repository.countByEnabledTrue();
	}
	
	
	public Page<Item> search(ItemSearchParam search) {
		Sort sort=Sort.by("id").descending();
		
		Pageable pageable = PageRequest.of(search.getPage(), search.getSize(), sort);
		
		Specification<Item> specification=like("name",search.getName()).and(equal("enabled",search.getEnabled()));
		
		return repository.findAll(specification,pageable);
	}
	

}
