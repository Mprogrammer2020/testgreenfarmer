package com.greenelegentfarmer.repository;

import java.util.List;
import com.greenelegentfarmer.entity.User;
import com.greenelegentfarmer.entity.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserOrderRepository extends JpaRepository<UserOrder, Long>,JpaSpecificationExecutor<UserOrder>{
	Long countByStatus(String status);
	
	List<UserOrder> findBySubscriptionUserAndStatusOrderByIdDesc(User user,String status);
	
	
}
