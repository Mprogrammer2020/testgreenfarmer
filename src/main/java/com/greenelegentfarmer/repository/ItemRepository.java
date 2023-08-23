package com.greenelegentfarmer.repository;

import com.greenelegentfarmer.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ItemRepository extends JpaRepository<Item, Long>,JpaSpecificationExecutor<Item> {

	Long countByEnabledTrue();
}
