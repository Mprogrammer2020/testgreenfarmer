package com.greenelegentfarmer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenelegentfarmer.entity.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long>{

}
