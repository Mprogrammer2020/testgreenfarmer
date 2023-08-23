package com.greenelegentfarmer.dto;

/*
Written By - raul__manendez
must be implmented by all those DTO where
DTO is being converted into an Entity
args :
	 E : the entity class for CRUD
*/
public interface DTOToEntity<E> {
	//implement this method to convert the DTO into corresponding entity
	E toEntity();
}
