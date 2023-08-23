package com.greenelegentfarmer.util;


import org.springframework.data.jpa.domain.Specification;

public interface SpecificationFactory<T> {
	
	default Specification<T> all() {
		return (root, cq, cb) -> cb.conjunction();
	}
	
	default Specification<T> like(String key,String value) {
		return (root, cq, cb) -> value==null ? cb.conjunction() : cb.like(root.get(key), "%" + value + "%");
	}
	
	default Specification<T> equal(String key,Object value) {
		return (root, cq, cb) -> value==null ? cb.conjunction() : cb.equal(root.get(key), value);
	}
	
}
