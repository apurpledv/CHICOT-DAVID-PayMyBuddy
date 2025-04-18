package com.openclassrooms.PayMyBuddy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.PayMyBuddy.controller.UserController;
import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.service.UserService;
import com.openclassrooms.PayMyBuddy.util.PMBUtil;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	UserController Controller;
	
	@MockitoBean
	UserService UserService;
	
	@BeforeEach
	void setupTests() {
		when(UserService.getUsers()).thenReturn(new ArrayList<User>());
	}
	
	@Test
	public void testGetUsers() throws Exception {
		this.mockMvc.perform(get("/user"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON));

		verify(UserService, Mockito.times(1)).getUsers();
	}
	
	@Test
	public void testGetUsersNonValid() throws Exception {
		// CASE#1 - Generic Exception Thrown
		when(UserService.getUsers()).thenAnswer(invocation -> { 
			throw new Exception(); 
		});
		
		this.mockMvc.perform(get("/user"))
			.andExpect(status().isInternalServerError());
	}
	
	@Test
	public void testAddUser() throws Exception {
		when(UserService.addUser(any(User.class))).thenReturn(true);
		
		String Body = "{\"user\": \"testUser\", \"email\": \"testUser@gmail.com\", \"password\": \"testUser86\"}";
		
		this.mockMvc.perform(post("/user")
			.contentType(PMBUtil.APPLICATION_JSON_UTF8)
			.content(Body)
		).andExpect(status().isOk());

		verify(UserService, Mockito.times(1)).addUser(any(User.class));
	}
	
	@Test
	public void testAddUserNonValid() throws Exception {
		String Body = "{\"user\": \"testUser\", \"email\": \"testUser@gmail.com\", \"password\": \"testUser86\"}";
		
		// CASE#1 - Generic Exception Thrown
		when(UserService.addUser(any(User.class))).thenAnswer(invocation -> { 
			throw new Exception(); 
		});
		
		this.mockMvc.perform(post("/user")
			.contentType(PMBUtil.APPLICATION_JSON_UTF8)
			.content(Body)
		).andExpect(status().isInternalServerError());
		
		// CASE#2 - Couldn't add
		when(UserService.addUser(any(User.class))).thenReturn(false);
		
		this.mockMvc.perform(post("/user")
			.contentType(PMBUtil.APPLICATION_JSON_UTF8)
			.content(Body)
		).andExpect(status().isInternalServerError());
	}
	
	@Test
	public void testUpdateUser() throws Exception {
		when(UserService.updateUser(any(User.class))).thenReturn(true);
		
		String Body = "{\"user\": \"testUser\", \"email\": \"testUser@gmail.com\", \"password\": \"testUser86\"}";
		
		this.mockMvc.perform(put("/user")
			.contentType(PMBUtil.APPLICATION_JSON_UTF8)
			.content(Body)
		).andExpect(status().isOk());

		verify(UserService, Mockito.times(1)).updateUser(any(User.class));
	}
	
	@Test
	public void testUpdateUserNonValid() throws Exception {
		String Body = "{\"user\": \"testUser\", \"email\": \"testUser@gmail.com\", \"password\": \"testUser86\"}";
		
		// CASE#1 - Generic Exception Thrown
		when(UserService.updateUser(any(User.class))).thenAnswer(invocation -> { 
			throw new Exception(); 
		});
		
		this.mockMvc.perform(put("/user")
			.contentType(PMBUtil.APPLICATION_JSON_UTF8)
			.content(Body)
		).andExpect(status().isInternalServerError());
		
		// CASE#2 - Couldn't update
		when(UserService.updateUser(any(User.class))).thenReturn(false);
		
		this.mockMvc.perform(put("/user")
			.contentType(PMBUtil.APPLICATION_JSON_UTF8)
			.content(Body)
		).andExpect(status().isInternalServerError());
	}
	
	@Test
	public void testDeleteUser() throws Exception {
		when(UserService.deleteUser(any(String.class))).thenReturn(true);
		
		this.mockMvc.perform(delete("/user?username=any_username"))
			.andExpect(status().isOk());

		verify(UserService, Mockito.times(1)).deleteUser(any(String.class));
	}
	
	@Test
	public void testDeleteUserNonValid() throws Exception {
		// CASE#1 - Couldn't delete
		when(UserService.deleteUser(any(String.class))).thenReturn(false);
		
		this.mockMvc.perform(delete("/user?username=any_username"))
			.andExpect(status().isInternalServerError());
		
		// CASE#2 - Wrong arguments
		when(UserService.deleteUser(any(String.class))).thenReturn(true);
		
		this.mockMvc.perform(delete("/user?username"))
			.andExpect(status().isBadRequest());
		
		this.mockMvc.perform(delete("/user"))
			.andExpect(status().isBadRequest());
		
		// CASE#3 - Generic Exception Thrown
		when(UserService.deleteUser(any(String.class))).thenAnswer(invocation -> { 
			throw new Exception(); 
		});
		
		this.mockMvc.perform(delete("/user?username=any_username"))
			.andExpect(status().isInternalServerError());
	}
}
