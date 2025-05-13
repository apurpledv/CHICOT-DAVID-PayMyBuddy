package com.openclassrooms.PayMyBuddy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.openclassrooms.PayMyBuddy.model.Connection;
import com.openclassrooms.PayMyBuddy.model.ConnectionId;
import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.repository.ConnectionRepository;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;
import com.openclassrooms.PayMyBuddy.service.ConnectionService;

@SpringBootTest
public class ConnectionServiceTest {
	@Autowired
	ConnectionService Service;
	
	@MockitoBean
	UserRepository UserRepo;

	@MockitoBean
	ConnectionRepository ConnectionRepo;
	
	@BeforeEach
	public void setupTests() throws Exception {
		List<Connection> DummyList = new ArrayList<Connection>();
		Connection DummyConnection = new Connection();
		ConnectionId DummyConnectionId = new ConnectionId();
		DummyConnectionId.setUserFrom(2);
		DummyConnectionId.setUserTo(3);
		DummyConnection.setConnectionId(DummyConnectionId);
		DummyConnection.setDateAdded("2025-04-04 08:00:00");
		DummyList.add(DummyConnection);
		
		when(ConnectionRepo.findAll()).thenReturn(DummyList);
	}
	
	@Test
	public void testGetAllConnections() {
		List<Connection> ConnectionsList = Service.getConnections();
		
		assertFalse(ConnectionsList.isEmpty());
		Connection Con = ConnectionsList.get(0);
		assertEquals(2, Con.getConnectionId().getUserFrom());
		assertEquals(3, Con.getConnectionId().getUserTo());
		assertEquals("2025-04-04 08:00:00", Con.getDateAdded());
	}
	
	@Test
	public void testGetConnectionsByUserFrom() {
		when(ConnectionRepo.findByUserFrom(any(int.class))).thenReturn(new ArrayList<Connection>());
		assertTrue(Service.getConnectionsByUserFrom(1) instanceof List);
	}
	
	@Test
	public void testGetConnectionsByUserTo() {
		when(ConnectionRepo.findByUserTo(any(int.class))).thenReturn(new ArrayList<Connection>());
		assertTrue(Service.getConnectionsByUserTo(1) instanceof List);
	}
	
	@Test
	public void testGetConnectionsByUserFromAndTo() {
		when(ConnectionRepo.findByUserFromAndTo(any(int.class), any(int.class))).thenReturn(new Connection());
		assertTrue(Service.getConnectionBetweenUsers(1, 2) instanceof Connection);
	}
	
	@Test
	public void testAddConnection() throws Exception {
		Connection DummyConnection2 = new Connection();
		ConnectionId DummyConnection2Id = new ConnectionId();
		DummyConnection2Id.setUserFrom(3);
		DummyConnection2Id.setUserTo(2);
		DummyConnection2.setConnectionId(DummyConnection2Id);
		DummyConnection2.setDateAdded("1999-04-04 08:00:00");
		
		// Adding the Connection
		assertTrue(Service.addConnection(DummyConnection2));
	}
	
	@Test
	public void testAddConnectionNonValid() {
		Connection DummyConnection2 = new Connection();
		ConnectionId DummyConnection2Id = new ConnectionId();
		DummyConnection2Id.setUserFrom(1);
		DummyConnection2Id.setUserTo(3);
		DummyConnection2.setConnectionId(DummyConnection2Id);

		// CASE#1 - Generic Exception
		when(ConnectionRepo.findByUserFromAndTo(any(int.class), any(int.class))).thenReturn(null);
		when(ConnectionRepo.addConnection(any(int.class), any(int.class))).thenAnswer(invocation -> { 
			throw new Exception(); 
		});
		
		assertThrows(Exception.class, () -> Service.addConnection(DummyConnection2));
	}
	
	@Test
	public void testDeleteConnection() {
		// Deleting the Connection
		assertTrue(Service.deleteConnection(99, 99));
	}

	@Test
	public void getUsersConnectedToUser() {
		List<Connection> ConList = new ArrayList<Connection>();
			Connection Con1 = new Connection();
				ConnectionId Con1Id = new ConnectionId();
				Con1Id.setUserFrom(1);
				Con1Id.setUserTo(2);
				Con1.setConnectionId(Con1Id);
			ConList.add(Con1);

			Connection Con2 = new Connection();
				ConnectionId Con2Id = new ConnectionId();
				Con2Id.setUserFrom(1);
				Con2Id.setUserTo(9999);
				Con2.setConnectionId(Con2Id);
			ConList.add(Con2);

		when(ConnectionRepo.findByUserFrom(any(int.class))).thenReturn(ConList);
		when(UserRepo.getById(eq(2))).thenReturn(new User());
		when(UserRepo.getById(eq(9999))).thenReturn(null);

		assertTrue(Service.getUsersConnectedToUser(any(int.class)) instanceof List);
	}

	@Test
	public void testCreateConnectionEntityFromEmail() {
		User User1 = new User();
		User1.setId(2);

		when(UserRepo.findByEmail(anyString())).thenReturn(User1);

		Connection CreatedCon = Service.createConnectionEntityFromEmail(1, "anyEmail");
		assertTrue(CreatedCon instanceof Connection);
		assertEquals(1, CreatedCon.getConnectionId().getUserFrom());
		assertEquals(2, CreatedCon.getConnectionId().getUserTo());
	}

	@Test
	public void testCreateConnectionEntityFromEmailNonValid() {
		User User1 = new User();
		User1.setId(2);

		// CASE#1 - No User found using the provided Email
		when(UserRepo.findByEmail(anyString())).thenReturn(null);
		assertEquals(null, Service.createConnectionEntityFromEmail(1, "anyEmail"));

		// CASE#2 - Same User From & To
		when(UserRepo.findByEmail(anyString())).thenReturn(User1);
		assertEquals(null, Service.createConnectionEntityFromEmail(2, "anyEmail"));
	}
}
