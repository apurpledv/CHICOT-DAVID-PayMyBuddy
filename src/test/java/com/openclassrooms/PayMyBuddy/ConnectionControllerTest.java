package com.openclassrooms.PayMyBuddy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.PayMyBuddy.controller.ConnectionController;
import com.openclassrooms.PayMyBuddy.model.Connection;
import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.service.ConnectionService;

@SpringBootTest
@AutoConfigureMockMvc
public class ConnectionControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	ConnectionController Controller;
	
	@MockitoBean
	ConnectionService ConnectionService;

	private HashMap<String, Object> SessionAttributes = new HashMap<String, Object>();
	
	@BeforeEach
	void setupTests() {
		SessionAttributes = new HashMap<String, Object>();
		SessionAttributes.put("userId", 9999);
	}
	
	@Test
	public void testGetRelationsView() throws Exception {
		this.mockMvc.perform(get("/relation")
			.sessionAttrs(SessionAttributes)
		).andExpect(status().isOk());
	}

	@Test
	public void testGetRelationsViewNonValid() throws Exception {
		// CASE#1 - No Session -> Redirection to Login
		this.mockMvc.perform(get("/relation")
		).andExpect(status().isFound());

		verify(ConnectionService, Mockito.times(0)).getUsersConnectedToUser(any(int.class));

		// CASE#2 - Generic Exception
		when(ConnectionService.getUsersConnectedToUser(any(int.class))).thenAnswer(invocation -> { 
			throw new Exception(); 
		});

		this.mockMvc.perform(get("/relation")
			.sessionAttrs(SessionAttributes)
		).andExpect(status().isOk());
	}

	@Test
	public void testPostRelationsView() throws Exception {
		User ValidEmail = new User();
		ValidEmail.setEmail("jackcurtis@gmail.com");

		when(ConnectionService.createConnectionEntityFromEmail(any(int.class), any(String.class))).thenReturn(new Connection());
		when(ConnectionService.addConnection(any(Connection.class))).thenReturn(true);

		this.mockMvc.perform(post("/createRelation")
			.sessionAttrs(SessionAttributes)
			.flashAttr("connectionForm", ValidEmail)
		).andExpect(status().isFound());
	}

	@Test
	public void testPostRelationsViewNonValid() throws Exception {
		User ValidEmail = new User();
		ValidEmail.setEmail("jackcurtis@gmail.com");

		// CASE#1 - No Session -> Redirection to Login
		this.mockMvc.perform(post("/createRelation")
		).andExpect(status().isFound());

		// CASE#2 - A problem occurred during the creation process of the Connection Entity
		when(ConnectionService.createConnectionEntityFromEmail(any(int.class), any(String.class))).thenReturn(null);
		
		this.mockMvc.perform(post("/createRelation")
			.sessionAttrs(SessionAttributes)
			.flashAttr("connectionForm", ValidEmail)
		).andExpect(status().isFound());

		// CASE#3 - A problem occurred when trying to add the Connection Entity into the App (already created successfully)
		when(ConnectionService.createConnectionEntityFromEmail(any(int.class), any(String.class))).thenReturn(new Connection());
		when(ConnectionService.addConnection(any(Connection.class))).thenReturn(false);

		this.mockMvc.perform(post("/createRelation")
			.sessionAttrs(SessionAttributes)
			.flashAttr("connectionForm", ValidEmail)
		).andExpect(status().isFound());

		// CASE#4 - Generic Exception
		when(ConnectionService.createConnectionEntityFromEmail(any(int.class), any(String.class))).thenAnswer(invocation -> { 
			throw new Exception(); 
		});

		this.mockMvc.perform(post("/createRelation")
			.sessionAttrs(SessionAttributes)
			.flashAttr("connectionForm", ValidEmail)
		).andExpect(status().isOk());
	}
}
