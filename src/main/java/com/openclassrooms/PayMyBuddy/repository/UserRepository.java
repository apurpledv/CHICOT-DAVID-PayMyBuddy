package com.openclassrooms.PayMyBuddy.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
 
import com.openclassrooms.PayMyBuddy.model.User;

/**
 * <p>UserRepository is an entity that handles Data directly for Users (Adding; Modifying; Removing..)</p>
 */
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
	/**
	 * <p>Returns an List of all User entities registered</p>
	 * @return a List of User entities
	 */
	public List<User> findAll();
	
	/**
	 * <p>Returns a User entity found using their Id</p>
	 * @param id The Unique identifier to search with
	 * @return a User Entity
	 */
	public User findById(int id);
	
	/**
	 * <p>Returns a User entity found using their Username</p>
	 * @param user The Username to search with
	 * @return a User Entity
	 */
	public User findByUser(String user);
	
	/**
	 * <p>Returns a User entity found using their Email</p>
	 * @param email The Email Address to search with
	 * @return a User Entity
	 */
	public User findByEmail(String email);
	
	/**
	 * <p>Returns a User entity found using their Username or their Email</p>
	 * @param user The Username to search with
	 * @param email The Email Address to search with
	 * @return a User Entity
	 */
	public User findByUserOrEmail(String user, String email);
	
	/**
	 * <p>Deletes a User entity identified with their Username</p>
	 * @param user The User's Username
	 */
	public void deleteByUser(String user);
}
