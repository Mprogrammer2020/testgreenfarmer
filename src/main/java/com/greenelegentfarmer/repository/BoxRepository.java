package com.greenelegentfarmer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.greenelegentfarmer.entity.Box;

@Repository
public interface BoxRepository  extends JpaRepository<Box, Long>{

}
