package com.openclassrooms.PayMyBuddy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import com.openclassrooms.PayMyBuddy.controller.ConnectionController;
import com.openclassrooms.PayMyBuddy.model.Connection;
import com.openclassrooms.PayMyBuddy.service.ConnectionService;
import com.openclassrooms.PayMyBuddy.util.ConnectionAlreadyExistsException;
import com.openclassrooms.PayMyBuddy.util.PMBUtil;

@SpringBootTest
@AutoConfigureMockMvc
public class ConnectionControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	ConnectionController Controller;
	
	@MockitoBean
	ConnectionService ConnectionService;
	
	@BeforeEach
	void setupTests() {
		when(ConnectionService.getConnections()).thenReturn(new ArrayList<Connection>());
	}
	
	@Test
	public void testGetConnections() throws Exception {
		this.mockMvc.perform(get("/connection"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON));

		verify(ConnectionService, Mockito.times(1)).getConnections();
	}
	
	@Test
	public void testGetConnectionsNonValid() throws Exception {
		// CASE#1 - Generic Exception Thrown
		when(ConnectionService.getConnections()).thenAnswer(invocation -> { 
			throw new Exception(); 
		});
		
		this.mockMvc.perform(get("/connection"))
			.andExpect(status().isInternalServerError());
	}
	
	@Test
	public void testAddConnection() throws Exception {
		when(ConnectionService.addConnection(any(Connection.class))).thenReturn(true);
		
		String Body = "{\"connectionId\": {\"userFrom\": 3, \"userTo\": 1}, \"dateAdded\": \"2025-04-04 08:00:00\"}";		
		this.mockMvc.perform(post("/connection")
			.contentType(PMBUtil.APPLICATION_JSON_UTF8)
			.content(Body)
		).andExpect(status().isOk());

		verify(ConnectionService, Mockito.times(1)).addConnection(any(Connection.class));
	}
	
	@Test
	public void testAddConnectionNonValid() throws Exception {
		String Body = "{\"connectionId\": {\"userFrom\": 3, \"userTo\": 1}, \"dateAdded\": \"2025-04-04 08:00:00\"}";

		// CASE#1 - Couldn't add
		when(ConnectionService.addConnection(any(Connection.class))).thenReturn(false);
		
		this.mockMvc.perform(post("/connection")
			.contentType(PMBUtil.APPLICATION_JSON_UTF8)
			.content(Body)
		).andExpect(status().isInternalServerError());
		
		// CASE#2 - ConnectionAlreadyExistsException is thrown
		when(ConnectionService.addConnection(any(Connection.class))).thenAnswer(invocation -> { 
			throw new ConnectionAlreadyExistsException(); 
		});
		
		this.mockMvc.perform(post("/connection")
			.contentType(PMBUtil.APPLICATION_JSON_UTF8)
			.content(Body)
		).andExpect(status().isInternalServerError());
		
		// CASE#3 - Generic Exception Thrown
		when(ConnectionService.addConnection(any(Connection.class))).thenAnswer(invocation -> { 
			throw new Exception(); 
		});
		
		this.mockMvc.perform(post("/connection")
			.contentType(PMBUtil.APPLICATION_JSON_UTF8)
			.content(Body)
		).andExpect(status().isInternalServerError());
	}
	
	@Test
	public void testDeleteConnection() throws Exception {
		when(ConnectionService.deleteConnection(any(int.class), any(int.class))).thenReturn(true);
		
		this.mockMvc.perform(delete("/connection?userFromId=99&userToId=999"))
			.andExpect(status().isOk());

		verify(ConnectionService, Mockito.times(1)).deleteConnection(any(int.class), any(int.class));
	}
	
	@Test
	public void testDeleteConnectionNonValid() throws Exception {
		// CASE#1 - Couldn't delete
		when(ConnectionService.deleteConnection(any(int.class), any(int.class))).thenReturn(false);

		this.mockMvc.perform(delete("/connection?userFromId=1&userToId=2"))
			.andExpect(status().isInternalServerError());
		
		// CASE#2 - Wrong arguments
		when(ConnectionService.deleteConnection(any(int.class), any(int.class))).thenReturn(true);
		
		this.mockMvc.perform(delete("/connection?userFromId=1"))
			.andExpect(status().isBadRequest());
		
		this.mockMvc.perform(delete("/connection?userToId=2"))
			.andExpect(status().isBadRequest());
		
		this.mockMvc.perform(delete("/connection"))
			.andExpect(status().isBadRequest());
		
		// CASE#3 - Generic Exception Thrown
		when(ConnectionService.deleteConnection(any(int.class), any(int.class))).thenAnswer(invocation -> { 
			throw new Exception(); 
		});
		
		this.mockMvc.perform(delete("/connection?userFromId=1&userToId=2"))
			.andExpect(status().isInternalServerError());
	}
}
