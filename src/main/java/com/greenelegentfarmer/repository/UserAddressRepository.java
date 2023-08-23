package com.greenelegentfarmer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.greenelegentfarmer.entity.User;
import com.greenelegentfarmer.entity.UserAddress;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long>,JpaSpecificationExecutor<UserAddress>{

	List<UserAddress> findByUserIdOrderByIdDesc(Long user_id);
}
