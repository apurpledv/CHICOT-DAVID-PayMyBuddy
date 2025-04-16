package com.openclassrooms.PayMyBuddy.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.PayMyBuddy.model.Connection;

/**
 * <p>ConnectionRepository is an entity that handles Data directly for Connections between Users (Adding; Modifying; Removing..)</p>
 */
@Repository
public interface ConnectionRepository extends CrudRepository<Connection, Integer> {
	/**
	 * <p>Returns an List of all Connection entities registered</p>
	 * @return a List of Connection entities
	 */
	public List<Connection> findAll();
}
