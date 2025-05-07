package com.openclassrooms.PayMyBuddy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;
import com.openclassrooms.PayMyBuddy.service.UserService;
import com.openclassrooms.PayMyBuddy.util.UserAlreadyExistsException;

@SpringBootTest
public class UserServiceTest {
	@Autowired
	UserService Service;
	
	@MockitoBean
	UserRepository UserRepo;
	
	@BeforeEach
	public void setupTests() throws Exception {
		List<User> DummyList = new ArrayList<User>();
		User DummyUser = new User();
		DummyUser.setUser("testUser");
		DummyUser.setEmail("testUser@gmail.com");
		DummyUser.setPassword("testUser99");
		DummyList.add(DummyUser);
		
		when(UserRepo.findAll()).thenReturn(DummyList);
	}
	
	@Test
	public void testGetUsers() {
		List<User> UsersList = Service.getUsers();
		
		assertFalse(UsersList.isEmpty());
		assertEquals("testUser", UsersList.get(0).getUser());
		assertEquals("testUser@gmail.com", UsersList.get(0).getEmail());
		assertEquals("testUser99", UsersList.get(0).getPassword());
	}
	
	@Test
	public void testGetUserByUsername() {
		when(UserRepo.findByUser(any(String.class))).thenReturn(new User());
		assertTrue(Service.getUserByUsername("Dummy") instanceof User);
	}
	
	@Test
	public void testAddUser() throws Exception {
		User DummyUser2 = new User();
		DummyUser2.setUser("dummyUser");
		DummyUser2.setEmail("dummyUser@gmail.com");
		DummyUser2.setPassword("dummyUserdummyUser");
		
		// Adding the User
		assertTrue(Service.addUser(DummyUser2));
	}
	
	@Test
	public void testAddUserNonValid() {
		// CASE#1 - User already exists (username OR email non unique)
		
		User DummyUser2 = new User();
		DummyUser2.setUser("dummyUser");
		DummyUser2.setEmail("dummyUser@gmail.com");
		DummyUser2.setPassword("dummyUserdummyUser");
		
		when(UserRepo.addUser(any(String.class), any(String.class), any(String.class))).thenAnswer(invocation -> { 
			throw new UserAlreadyExistsException(); 
		});
		
		// Adding the 'Duplicate' User
		assertThrows(UserAlreadyExistsException.class, () -> Service.addUser(DummyUser2));
	}
	
	@Test
	public void testUpdateUser() {
		User DummyUser2 = new User();
		DummyUser2.setUser("JackCurtis");
		DummyUser2.setEmail("jackcurtis@gmail.com");
		DummyUser2.setPassword("newPassword");
		
		when(UserRepo.findByUserOrEmail(any(String.class), any(String.class))).thenReturn(DummyUser2);
		
		// Updating the User
		assertTrue(Service.updateUser(DummyUser2));
	}
	
	@Test
	public void testUpdateUserNonValid() {
		// CASE#1 - User doesn't exist
		User DummyUser2 = new User();
		DummyUser2.setUser("dummyUser");
		DummyUser2.setEmail("dummyUser@gmail.com");
		DummyUser2.setPassword("dummyUserdummyUser");
		
		when(UserRepo.findByUserOrEmail(any(String.class), any(String.class))).thenReturn(null);
		
		// Updating the Non-Existant the User
		assertFalse(Service.updateUser(DummyUser2));
	}
	
	@Test
	public void testDeleteUser() {
		// Deleting the User
		assertTrue(Service.deleteUser("any_username"));
	}

	@Test
	public void testCreatePassword() {
		String RawPassword = "RawPassword7412025";
		String CreatedPassword = Service.createPassword(RawPassword);

		assertFalse(RawPassword.equals(CreatedPassword));
	}

	@Test
	public void testVerifyPassword() {
		String RawPassword = "RawPassword7412025";
		String CreatedPassword = Service.createPassword(RawPassword);

		assertTrue(Service.verifyPassword(RawPassword, CreatedPassword));
		assertFalse(Service.verifyPassword("WrongRawPassword", CreatedPassword));
	}
}
