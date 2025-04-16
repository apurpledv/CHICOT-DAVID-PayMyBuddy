package com.openclassrooms.PayMyBuddy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;

/**
 * <p>UserService is an entity that handles the work with Users</p>
 */
@Service
public class UserService {
	@Autowired
	UserRepository UserRepo;
	
	/**
	 * <p>Returns a List of User entities registered in the App</p>
	 * @return a List of User entities
	 */
	public List<User> getUsers() {
		return UserRepo.findAll();
	}
	
	/**
	 * <p>Returns a User entity identified by their Username</p>
	 * @return a User entity
	 */
	public User getUserByUsername(String username) {
		return UserRepo.findByUser(username);
	}
	
	/**
	 * <p>Creates a new User entity, or Updates the data for an existing User</p>
	 * @param user a User Entity to add (if new) or update
	 * @return true if everything went right
	 */
	public boolean addUser(User user) {
		UserRepo.save(user);
		return true;
	}
	
	/**
	 * <p>Creates a new User entity, or Updates the data for an existing User</p>
	 * @param user a User Entity to add (if new) or update
	 * @return true if everything went right; false if the User doesn't exist
	 */
	public boolean updateUser(User newUserData) {
		User User = UserRepo.findByUserOrEmail(newUserData.getUser(), newUserData.getEmail());
		if (User == null)
			return false;
		
		User.setUser(newUserData.getUser());
		User.setEmail(newUserData.getEmail());
		User.setPassword(newUserData.getPassword());
		UserRepo.save(User);
		return true;
	}
	
	public boolean deleteUser(String username) {
		UserRepo.deleteByUser(username);
		return true;
	}
}
