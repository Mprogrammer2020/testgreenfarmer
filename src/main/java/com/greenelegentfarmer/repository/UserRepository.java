package com.greenelegentfarmer.repository;

import org.springframework.stereotype.Repository;

import com.greenelegentfarmer.entity.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface UserRepository extends JpaRepository<User, Long>,JpaSpecificationExecutor<User> {
	
	List<User> findByStatus(String status);
	
	List<User> findByUnLockedTrueAndRole(String role);
	
	Page<User> findAllByRole(String role,Pageable pageable);

	
	@Query("SELECT COUNT(id) FROM User U where U.role=:role")
	long countByRole(String role);
	
	@Query("SELECT U FROM User U LEFT JOIN U.addresses where (U.email=:emailOrPhone OR U.phone=:emailOrPhone)")
	Optional<User> findByEmailOrPhone(String emailOrPhone);
	
}
