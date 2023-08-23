package com.greenelegentfarmer.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.greenelegentfarmer.constant.Constants;
import com.greenelegentfarmer.entity.UserOrder;
import com.greenelegentfarmer.entity.UserOrderTransaction;
import com.greenelegentfarmer.param.UserOrderTransactionParam;
import com.greenelegentfarmer.repository.UserOrderTransactionRepository;
import com.greenelegentfarmer.specification.UserOrderTransactionSpecification;
import com.greenelegentfarmer.util.CrudService;
import com.stripe.model.Charge;

@Service
public class UserOrderTransactionService extends CrudService<UserOrderTransaction> implements UserOrderTransactionSpecification {
	
	private UserOrderTransactionRepository repository;
	public UserOrderTransactionService(UserOrderTransactionRepository repository) {
		super(repository);
		this.repository=repository;
	}

	public Long sale() {
		return repository.sale();
	}

	
	public void createTransaction(UserOrder order,Charge charge) {
		UserOrderTransaction transaction=new UserOrderTransaction();
		transaction.setOrder(order);

		if(charge!=null) {
			transaction.setChargeId(charge.getId());
			transaction.setAmount(charge.getAmountCaptured()/100);
			transaction.setStatus(Constants.SUCCESS);
		} else {
			transaction.setStatus(Constants.FAILED);
		}
		
		add(transaction);
	}
	
	public Page<UserOrderTransaction> filter(UserOrderTransactionParam param) {

		Sort sort = Sort.by("id").descending();

		Pageable pageable = PageRequest.of(param.getPage(), param.getSize(), sort);

		Specification<UserOrderTransaction> specification = filterSpecification(param);

		return repository.findAll(specification, pageable);
	}
	
}
