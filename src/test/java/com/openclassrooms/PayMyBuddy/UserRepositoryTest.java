package com.openclassrooms.PayMyBuddy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;

@SpringBootTest
class UserRepositoryTest {
	@Autowired
	UserRepository UserRepo;
	
	private User DummyUser;
	
	@BeforeEach
	void setupTests() throws Exception {
		DummyUser = new User();
		DummyUser.setUser("testUser");
		DummyUser.setEmail("testUser@gmail.com");
		DummyUser.setPassword("testUser99");
	}

	@Test
	public void testGetAllUsers() {
		assertTrue(UserRepo.findAll().size() > 0);
	}
	
	@Test
	public void testGetOneUserById() {
		assertTrue(UserRepo.findById(1) != null);
	}
	
	@Test
	public void testGetOneUserByUsername() {
		assertTrue(UserRepo.findByUser("JackCurtis") != null);
	}
	
	@Test
	public void testGetOneUserByEmail() {
		assertTrue(UserRepo.findByEmail("jackcurtis@gmail.com") != null);
	}
	
	@Test
	public void testGetOneUserByUserOrEmail() {
		assertTrue(UserRepo.findByUserOrEmail("JackCurtis", "jackcurtis@gmail.com") != null);
	}
	
	@Test
	public void testAddAndDeleteUser() {
		// Adding the User
		UserRepo.save(DummyUser);
		assertTrue(UserRepo.findByUser("testUser") != null);
		
		// Deleting the User
		UserRepo.delete(DummyUser);
		assertTrue(UserRepo.findByUser("testUser") == null);
	}
	
	@Test
	public void testModifyUser() {
		// Adding the User
		UserRepo.save(DummyUser);
		
		// Updating the User
		DummyUser.setPassword("testUser77777");
		UserRepo.save(DummyUser);
		assertEquals("testUser77777", UserRepo.findByUser("testUser").getPassword());
		
		// Clean Up
		UserRepo.delete(DummyUser);
		assertTrue(UserRepo.findByUser("testUser") == null);
	}
}
