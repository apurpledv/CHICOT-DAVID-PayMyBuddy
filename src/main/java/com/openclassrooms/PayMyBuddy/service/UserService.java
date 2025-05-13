package com.openclassrooms.PayMyBuddy.service;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.hash.Hashing;
import com.openclassrooms.PayMyBuddy.model.Mapper;
import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.repository.ConnectionRepository;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;

/**
 * <p>UserService is an entity that handles the work with Users</p>
 */
@Service
public class UserService {
	@Autowired
	UserRepository UserRepo;

	@Autowired
	ConnectionRepository ConnectionRepo;

	@Autowired
	Mapper MapperDTO;
	
	/**
	 * <p>Returns a List of User entities registered in the App</p>
	 * @return a List of User entities
	 */
	public List<User> getUsers() {
		return UserRepo.findAll();
	}

	/**
	 * <p>Returns a User entity identified by their Id</p>
	 * @return a User entity
	 */
	public User getUserById(int userId) {
		return UserRepo.getById(userId);
	}
	
	/**
	 * <p>Returns a User entity identified by their Username</p>
	 * @return a User entity
	 */
	public User getUserByUsername(String username) {
		return UserRepo.findByUser(username);
	}

	/**
	 * <p>Returns a User entity identified by their Email address</p>
	 * @return a User entity
	 */
	public User getUserByEmail(String email) {
		return UserRepo.findByEmail(email);
	}
	
	/**
	 * <p>Creates a new User entity</p>
	 * @param user a User Entity to add
	 * @return true if everything went right
	 */
	public boolean addUser(User user) {
		// Hashing the sensible data (password)
		user.setPassword(createPassword(user.getPassword()));

		UserRepo.addUser(user.getUser(), user.getEmail(), user.getPassword());
		return true;
	}
	
	/**
	 * <p>Updates the data for an existing User</p>
	 * @param user a User Entity to update
	 * @return true if everything went right; false if the User doesn't exist
	 */
	public boolean updateUser(User newUserData) {
		User User = UserRepo.getById(newUserData.getId());

		if (User == null)
			return false;
		
		if (newUserData.getUser() != null && !newUserData.getUser().isEmpty())
			User.setUser(newUserData.getUser());
		
		if (newUserData.getEmail() != null && !newUserData.getEmail().isEmpty())
			User.setEmail(newUserData.getEmail());
		
		if (newUserData.getPassword() != null && !newUserData.getPassword().isEmpty())
			User.setPassword(createPassword(newUserData.getPassword()));

		UserRepo.updateUser(User.getUser(), User.getEmail(), User.getPassword(), User.getId());
		return true;
	}
	
	/**
	 * <p>Deletes an existing User</p>
	 * @param username the username to look for
	 * @return true if everything went right
	 */
	public boolean deleteUser(String username) {
		UserRepo.deleteByUser(username);
		return true;
	}

	/**
	 * <p>Hashes a given string</p>
	 * @param rawPassword the string to be hashed
	 * @return the hashed string
	 */
	public String createPassword(String rawPassword) {
		return Hashing.sha256().hashString(rawPassword, StandardCharsets.UTF_8).toString();
	}

	/**
	 * <p>Compares two passwords: one hashed, and one not</p>
	 * @param rawPassword the raw string to be compared (will automatically hash it)
	 * @param hashedPassword the already hashed string
	 * @return true if they are the same; false if not
	 */
	public boolean verifyPassword(String rawPassword, String hashedPassword) {
		return hashedPassword.equals(createPassword(rawPassword));
	}

	/**
	 * <p>Retrieves a User entity with their email, then verifies their password's authenticity</p>
	 * @param email the email of the User to look for 
	 * @param password raw password to verify
	 * @return true if the User is authentified; false if the User doesn't exist OR doesn't have the right password
	 */
	public boolean verifyUser(String email, String password) {
		User UserToVerify = getUserByEmail(email);
		if (UserToVerify == null)
			return false;

		return verifyPassword(password, UserToVerify.getPassword());
	}
}
