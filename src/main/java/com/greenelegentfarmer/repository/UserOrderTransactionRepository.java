package com.greenelegentfarmer.repository;

import com.greenelegentfarmer.entity.UserOrderTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface UserOrderTransactionRepository extends JpaRepository<UserOrderTransaction, Long>,JpaSpecificationExecutor<UserOrderTransaction> {
	
	@Query("SELECT SUM(amount) FROM UserOrderTransaction UOT WHERE UOT.status='SUCCESS'")
	Long sale();
}
