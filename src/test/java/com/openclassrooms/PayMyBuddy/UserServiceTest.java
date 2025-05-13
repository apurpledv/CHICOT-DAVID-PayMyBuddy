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
	public void testGetUserById() {
		when(UserRepo.getById(any(int.class))).thenReturn(new User());
		assertTrue(Service.getUserById(1) instanceof User);
	}
	
	@Test
	public void testGetUserByUsername() {
		when(UserRepo.findByUser(any(String.class))).thenReturn(new User());
		assertTrue(Service.getUserByUsername("Dummy") instanceof User);
	}

	@Test
	public void testGetUserByEmail() {
		when(UserRepo.findByEmail(any(String.class))).thenReturn(new User());
		assertTrue(Service.getUserByEmail("Dummy") instanceof User);
	}
	
	@Test
	public void testAddUser() {
		User DummyUser2 = new User();
		DummyUser2.setUser("dummyUser");
		DummyUser2.setEmail("dummyUser@gmail.com");
		DummyUser2.setPassword("dummyUserdummyUser");
		
		// Adding the User
		assertTrue(Service.addUser(DummyUser2));
	}
	
	@Test
	public void testAddUserNonValid() {
		User DummyUser2 = new User();
		DummyUser2.setUser("dummyUser");
		DummyUser2.setEmail("dummyUser@gmail.com");
		DummyUser2.setPassword("dummyUserdummyUser");

		// CASE#1 - Generic Exception
		when(UserRepo.addUser(any(String.class), any(String.class), any(String.class))).thenAnswer(invocation -> { 
			throw new Exception(); 
		});
		
		assertThrows(Exception.class, () -> Service.addUser(DummyUser2));
	}
	
	@Test
	public void testUpdateUser() {
		User DummyUser2Base = new User();
		DummyUser2Base.setUser("JackCurtis");
		DummyUser2Base.setEmail("jackcurtis@gmail.com");
		DummyUser2Base.setPassword("newPassword");

		User DummyUser2Changes = new User();
		DummyUser2Changes.setUser("");
		DummyUser2Changes.setEmail("");
		DummyUser2Changes.setPassword("");

		when(UserRepo.getById(any(int.class))).thenReturn(DummyUser2Base);

		// CASE#1 - Nothing changes
		assertTrue(Service.updateUser(DummyUser2Changes));

		// CASE#2 - Partial changes
		DummyUser2Changes.setUser("user");
		assertTrue(Service.updateUser(DummyUser2Changes));
		assertEquals("user", DummyUser2Base.getUser());

		DummyUser2Changes.setEmail("email");
		assertTrue(Service.updateUser(DummyUser2Changes));
		assertEquals("email", DummyUser2Base.getEmail());

		DummyUser2Changes.setPassword("password");
		assertTrue(Service.updateUser(DummyUser2Changes));
		assertFalse(DummyUser2Base.getPassword().equals(""));
	}
	
	@Test
	public void testUpdateUserNonValid() {
		// CASE#1 - User doesn't exist
		User DummyUser2 = new User();
		DummyUser2.setUser("dummyUser");
		DummyUser2.setEmail("dummyUser@gmail.com");
		DummyUser2.setPassword("dummyUserdummyUser");
		
		when(UserRepo.getById(any(int.class))).thenReturn(null);
		
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

	@Test
	public void testVerifyUser() {
		User DummyUser2 = new User();
		DummyUser2.setUser("dummyUser");
		DummyUser2.setEmail("dummyUser@gmail.com");
		DummyUser2.setPassword(Service.createPassword("dummyUserdummyUser"));
		
		when(UserRepo.findByEmail(any(String.class))).thenReturn(DummyUser2);

		assertTrue(Service.verifyUser("dummyUser@gmail.com", "dummyUserdummyUser"));
	}

	@Test
	public void testVerifyUserNonValid() {
		when(UserRepo.findByEmail(any(String.class))).thenReturn(null);

		assertFalse(Service.verifyUser("anyEmail", "anyPassword"));
	}
}
