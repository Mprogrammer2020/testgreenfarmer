package com.greenelegentfarmer.specification;

import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import com.greenelegentfarmer.entity.UserOrderTransaction;
import com.greenelegentfarmer.param.UserOrderTransactionParam;

public interface UserOrderTransactionSpecification {

	default Specification<UserOrderTransaction> filterSpecification(UserOrderTransactionParam param) {
		return (root, cq, cb) -> {
		
		Predicate statusPredicate=param.getStatus()==null ? cb.conjunction() : cb.equal(root.get("status"), param.getStatus());
		Predicate userPredicate=param.getUser()==null ? cb.conjunction() : cb.equal(root.get("order").get("subscription").get("user"), param.getUser());
		
		Predicate deliveryDatePredicate=cb.conjunction();
		
		if(param.getFrom()!=null && param.getTo()!=null) {
			deliveryDatePredicate=cb.between(
					root.get("order").get("deliveryDate"), 
					LocalDateTime.of(param.getFrom(), LocalTime.MIN),
					LocalDateTime.of(param.getTo(), LocalTime.MAX));
		}
		
		return cb.and(statusPredicate,userPredicate,deliveryDatePredicate);
	};
}
}
