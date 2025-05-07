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
	 * <p>Returns a User entity identified by their Id</p>
	 * @return a User entity
	 */
	public User getUserById(int userId) {
		return UserRepo.findById(userId);
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
		if (user.getId() != 0)
			return false;

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
		User User = UserRepo.findById(newUserData.getId());

		if (User == null)
			return false;
		
		if (newUserData.getUser() != null && !newUserData.getUser().isEmpty())
			User.setUser(newUserData.getUser());
		
		if (newUserData.getEmail() != null && !newUserData.getEmail().isEmpty())
			User.setEmail(newUserData.getEmail());
		
		if (newUserData.getPassword() != null && !newUserData.getPassword().isEmpty())
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

	public boolean verifyUser(String email, String password) {
		User UserToVerify = getUserByEmail(email);
		if (UserToVerify == null)
			return false;

		return verifyPassword(password, UserToVerify.getPassword());
	}
}
