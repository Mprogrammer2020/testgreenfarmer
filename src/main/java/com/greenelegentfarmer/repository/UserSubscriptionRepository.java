package com.greenelegentfarmer.repository;

import com.greenelegentfarmer.entity.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long>{

	@Query("FROM UserSubscription US WHERE US.user.id=:user_id AND US.status NOT IN ('INACTIVE')")
	public UserSubscription mySubscription(Long user_id);
}
