package com.greenelegentfarmer.service;

import org.springframework.stereotype.Service;

import com.greenelegentfarmer.entity.Activity;
import com.greenelegentfarmer.repository.ActivityRepository;
import com.greenelegentfarmer.util.CrudService;

@Service
public class ActivityService extends CrudService<Activity> {

	public ActivityService(ActivityRepository repository) {
		super(repository);
	}

}
