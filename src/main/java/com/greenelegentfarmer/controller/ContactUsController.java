package com.greenelegentfarmer.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.greenelegentfarmer.entity.ContactUs;
import com.greenelegentfarmer.service.ContactUsService;
import com.greenelegentfarmer.util.CrudController;

@RestController
@RequestMapping("/contact-us")
public class ContactUsController extends CrudController<ContactUs> {

	public ContactUsController(ContactUsService service) {
		super(service);
	}
}
