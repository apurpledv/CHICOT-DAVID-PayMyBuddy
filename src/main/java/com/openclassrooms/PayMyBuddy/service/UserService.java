package com.openclassrooms.PayMyBuddy.service;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import com.google.common.hash.Hashing;
import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;
import com.openclassrooms.PayMyBuddy.util.UserAlreadyExistsException;

/**
 * <p>UserService is an entity that handles the work with Users</p>
 */
@Service
public class UserService {
	@Autowired
	UserRepository UserRepo;

	/*@Autowired
	private PasswordEncoder passwordEncoder;*/
	
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
	 * <p>Creates a new User entity</p>
	 * @param user a User Entity to add
	 * @return true if everything went right
	 */
	public boolean addUser(User user) throws Exception {
		if (user.getId() != 0)
			return false;
		
		if (UserRepo.findByUser(user.getUser()) != null || UserRepo.findByEmail(user.getEmail()) != null)
			throw new UserAlreadyExistsException();

		// Hashing the sensible data (password)
		user.setPassword(createPassword(user.getPassword()));

		UserRepo.save(user);
		return true;
	}
	
	/**
	 * <p>Updates the data for an existing User</p>
	 * @param user a User Entity to update
	 * @return true if everything went right; false if the User doesn't exist
	 */
	public boolean updateUser(User newUserData) {
		User User = UserRepo.findByUserOrEmail(newUserData.getUser(), newUserData.getEmail());
		if (User == null)
			return false;
		
		User.setUser(newUserData.getUser());
		User.setEmail(newUserData.getEmail());
		User.setPassword(createPassword(newUserData.getPassword()));
		UserRepo.save(User);
		return true;
	}
	
	public boolean deleteUser(String username) {
		UserRepo.deleteByUser(username);
		return true;
	}

	public String createPassword(String rawPassword) {
		return Hashing.sha256().hashString(rawPassword, StandardCharsets.UTF_8).toString();
	}

	public boolean verifyPassword(String rawPassword, String hashedPassword) {
		return hashedPassword.equals(createPassword(rawPassword));
	}
}
