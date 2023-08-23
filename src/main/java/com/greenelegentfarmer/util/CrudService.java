package com.greenelegentfarmer.util;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.greenelegentfarmer.entity.Item;
import com.greenelegentfarmer.exception.InvalidIDException;
import com.greenelegentfarmer.param.UserOrderSearchParam;

/*
	Written By - raul__manendez
	An abstract utility service to easily get all CRUD
	operations to child implementation. override the behavior if needed
	args :
		 T : the entity class for CRUD
*/
public abstract class CrudService<T> {
	
	@Autowired
	protected JpaRepository<T, Long> repository;
	
	public CrudService(JpaRepository<T, Long> repository) {
		this.repository=repository;
	}
	
	public Optional<T> getById(Long id) {
		return repository.findById(id);
	}
	
	public T expect(Long id) throws InvalidIDException {
		Optional<T> optional=getById(id);
		if(!optional.isPresent())
			throw new InvalidIDException();
		
		return optional.get(); 
				
	}
	
	public T add(T type) {
		return repository.save(type);
	}
	
	public List<T> addAll(List<T> entities) {
		return repository.saveAll(entities);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}
	
	public List<T> getAll() {
		return repository.findAll();
	}
	
	public Page<T> getAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	
}
