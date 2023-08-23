package com.greenelegentfarmer.specification;

import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import com.greenelegentfarmer.constant.Constants;
import com.greenelegentfarmer.entity.UserOrder;
import com.greenelegentfarmer.param.UserOrderSearchParam;
import com.greenelegentfarmer.util.SpecificationFactory;

public interface UserOrderSpecification extends SpecificationFactory<UserOrder> {

	default Specification<UserOrder> filterSpecification(UserOrderSearchParam param) {
		return (root, cq, cb) -> {
		
		Predicate statusPredicate=null;
		if(param.getStatus()==null) {
			statusPredicate=root.get("status").in(Constants.INITIATED);
		} else {
			statusPredicate=cb.equal(root.get("status"), param.getStatus());
		}
	
		
		Predicate boxPredicate=param.getBox()==null ? cb.conjunction() : cb.equal(root.get("subscription").get("box").get("id"), param.getBox());
		Predicate finalPredicate=cb.and(statusPredicate,boxPredicate);
		
		if(param.getFrom()!=null && param.getTo()!=null) {
			Predicate deliveryDatePredicate=cb.between(
					root.get("deliveryDate"), 
					LocalDateTime.of(param.getFrom(), LocalTime.MIN),
					LocalDateTime.of(param.getTo(), LocalTime.MAX));
			finalPredicate=cb.and(finalPredicate,deliveryDatePredicate);
		}
		
		String value=param.getSearch();
		if(value!=null) {
			Predicate firstNamePredicate=cb.like(root.get("subscription").get("user").get("firstName"), "%" + value + "%");
			Predicate lastNamePredicate=cb.like(root.get("subscription").get("user").get("lastName"), "%" + value + "%");
			
			Expression<String> fullNameExpression=cb.concat(root.get("subscription").get("user").get("firstName")," ");
				fullNameExpression=cb.concat(fullNameExpression, root.get("subscription").get("user").get("lastName"));
			Predicate fullNamePredicate=cb.equal(fullNameExpression , value);
			
			Predicate addressPredicate=cb.like(root.get("address").get("address"), "%" + value + "%");
			Predicate cityPredicate=cb.like(root.get("address").get("city"), "%" + value + "%");
			Predicate statePredicate=cb.like(root.get("address").get("state"), "%" + value + "%");
			Predicate zipCodePredicate=cb.like(root.get("address").get("zipCode"), "%" + value + "%");
			
			Predicate searchPredicate=cb.or(firstNamePredicate,lastNamePredicate,fullNamePredicate,addressPredicate,cityPredicate,statePredicate,zipCodePredicate);
			
			finalPredicate=cb.and(finalPredicate,searchPredicate);
		}
		
		return finalPredicate;
		};	
	}
}
