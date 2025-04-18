package com.openclassrooms.PayMyBuddy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.PayMyBuddy.model.Connection;

/**
 * <p>ConnectionRepository is an entity that handles Data directly for Connections between Users (Adding; Modifying; Removing..)</p>
 */
@Repository
public interface ConnectionRepository extends CrudRepository<Connection, Integer> {
	/**
	 * <p>Returns a List of all Connection entities registered</p>
	 * @return a List of Connection entities
	 */
	public List<Connection> findAll();
	
	/**
	 * <p>Returns a List of all Connection entities found using the date they'd been saved</p>
	 * @return a List of Connection entities
	 */
	public List<Connection> findByDateAdded(String dateAdded);
	
	/**
	 * <p>Returns a List of all Connection entities linked from a User Id</p>
	 * @return a List of Connection entities
	 */
	@Query(value = "SELECT * FROM t_connection WHERE user_from = ?1", nativeQuery = true)
	public List<Connection> findByUserFrom(int userId);
	
	/**
	 * <p>Returns a List of all Connection entities linked towards a User Id</p>
	 * @return a List of Connection entities
	 */
	@Query(value = "SELECT * FROM t_connection WHERE user_to = ?1", nativeQuery = true)
	public List<Connection> findByUserTo(int userId);
	
	/**
	 * <p>Returns a Connection entity linking two User Ids</p>
	 * @param userFromId the Id of the User the connection stems from
	 * @param userToId the Id of the User the connection is linked to
	 * @return a unique Connection entity between two Users
	 */
	@Query(value = "SELECT * FROM t_connection WHERE user_from = ?1 AND user_to = ?2", nativeQuery = true)
	public Connection findByUserFromAndTo(int userFromId, int userToId);
	
	/**
	 * <p>Deletes an Connection entity linking two User Ids</p>
	 * @param userFromId the Id of the User the connection stems from
	 * @param userToId the Id of the User the connection is linked to
	 */
	
	@Modifying
	@Query(value = "DELETE FROM t_connection WHERE user_from = ?1 AND user_to = ?2", nativeQuery = true)
	public void deleteByUserFromAndTo(int userFromId, int userToId);
}
