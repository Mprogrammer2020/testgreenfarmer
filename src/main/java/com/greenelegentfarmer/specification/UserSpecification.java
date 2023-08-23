package com.greenelegentfarmer.specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import com.greenelegentfarmer.entity.User;
import com.greenelegentfarmer.entity.UserAddress;
import com.greenelegentfarmer.param.UserSearchParam;
import com.greenelegentfarmer.util.SpecificationFactory;

public interface UserSpecification extends SpecificationFactory<User>  {

	default Specification<User> filterSpecification(UserSearchParam param) {
			return (root, cq, cb) -> {
			
			Join<User,UserAddress> address = root.join("addresses",JoinType.LEFT);
			
			Predicate rolePredicate=param.getRole()==null ? cb.conjunction() : cb.equal(root.get("role"), param.getRole());
			Predicate activePredicate=param.getEnabled()==null ? cb.conjunction() : cb.equal(root.get("enabled"), param.getEnabled());
			Predicate finalPredicate=cb.and(rolePredicate,activePredicate);
			
			String value=param.getSearch();
			if(value!=null) {
				Predicate firstNamePredicate=cb.like(root.get("firstName"), "%" + value + "%");
				Predicate lastNamePredicate=cb.like(root.get("lastName"), "%" + value + "%");
				
				Expression<String> fullNameExpression=cb.concat(root.get("firstName")," ");
				fullNameExpression=cb.concat(fullNameExpression, root.get("lastName"));
				Predicate fullNamePredicate=cb.equal(fullNameExpression , value);
			
				Predicate phonePredicate=cb.like(root.get("phone"), "%" + value + "%");
				
				Predicate addressPredicate=cb.like(address.get("address"), "%" + value + "%");
				Predicate cityPredicate=cb.like(address.get("city"), "%" + value + "%");
				Predicate statePredicate=cb.like(address.get("state"), "%" + value + "%");
				Predicate zipCodePredicate=cb.like(address.get("zipCode"), "%" + value + "%");
				
				Predicate searchPredicate=cb.or(firstNamePredicate,lastNamePredicate,fullNamePredicate,phonePredicate,addressPredicate,cityPredicate,statePredicate,zipCodePredicate);
				
				finalPredicate=cb.and(finalPredicate,searchPredicate);
			}
			
			return finalPredicate;
		};
	}
}
