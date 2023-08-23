package com.greenelegentfarmer.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import com.greenelegentfarmer.entity.DeliveryCharges;
import com.greenelegentfarmer.repository.DeliveryChargesRepository;
import com.greenelegentfarmer.util.CrudService;

@Service
public class DeliveryChargesService extends CrudService<DeliveryCharges> {

	
	public DeliveryChargesService(DeliveryChargesRepository repository) {
		super(repository);
	}
	
	public Float getDeliveryCharges() {
		Optional<DeliveryCharges> chargesOptional=getAll().stream().findFirst();
		return chargesOptional.isPresent() ? chargesOptional.get().getAmount() : 0;
	}

}
