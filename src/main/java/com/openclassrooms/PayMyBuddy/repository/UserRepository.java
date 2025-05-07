package com.openclassrooms.PayMyBuddy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
	 * <p>Adds a User entity</p>
	 * @param user username of the User
	 * @param email email of the User
	 * @param password password of the User
	 */
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO t_user (user, email, password) VALUES (?1, ?2, ?3)", nativeQuery = true)
	public int addUser(String user, String email, String password);

	/**
	 * <p>Updates a User entity</p>
	 * @param user username of the User
	 * @param email email of the User
	 * @param password password of the User
	 */
	/*@Modifying
	@Transactional
	@Query(value = "UPDATE t_user SET user = ?1, email = ?2, password = ?3 WHERE CustomerID = 1;", nativeQuery = true)
	public int updateUser(String user, String email, String password);*/

	/**
	 * <p>Deletes a User entity identified with their Username</p>
	 * @param user The User's Username
	 */
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM t_user WHERE user = ?1", nativeQuery = true)
	public void deleteByUser(String user);
}
