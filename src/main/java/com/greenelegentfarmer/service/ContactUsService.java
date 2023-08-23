package com.greenelegentfarmer.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.greenelegentfarmer.entity.ContactUs;
import com.greenelegentfarmer.repository.ContactUsRepository;
import com.greenelegentfarmer.util.CrudService;

@Service
public class ContactUsService extends CrudService<ContactUs> {

	public ContactUsService(ContactUsRepository repository) {
		super(repository);
	}
	
	@Override
	public Page<ContactUs> getAll(Pageable pageable) {
		Sort sort=Sort.by("id").descending();
		
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		return repository.findAll(pageable);
	}
}
