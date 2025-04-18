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

import com.openclassrooms.PayMyBuddy.model.Connection;
import com.openclassrooms.PayMyBuddy.model.ConnectionId;
import com.openclassrooms.PayMyBuddy.repository.ConnectionRepository;
import com.openclassrooms.PayMyBuddy.service.ConnectionService;

@SpringBootTest
public class ConnectionServiceTest {
	@Autowired
	ConnectionService Service;
	
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
	public void testGetConnections() {
		List<Connection> ConnectionsList = Service.getConnections();
		
		assertFalse(ConnectionsList.isEmpty());
		Connection Con = ConnectionsList.get(0);
		assertEquals(2, Con.getConnectionId().getUserFrom());
		assertEquals(3, Con.getConnectionId().getUserTo());
		assertEquals("2025-04-04 08:00:00", Con.getDateAdded());
	}
	
	@Test
	public void testAddConnection() {
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
		// CASE#1 - Connection already exists (UserFrom/UserTo combo already exists (ie: if 3-2 exists, 2-3 is still possible)
		
		Connection DummyConnection2 = new Connection();
		ConnectionId DummyConnection2Id = new ConnectionId();
		DummyConnection2Id.setUserFrom(3);
		DummyConnection2Id.setUserTo(2);
		DummyConnection2.setConnectionId(DummyConnection2Id);
		DummyConnection2.setDateAdded("1999-04-04 08:00:00");
		
		when(ConnectionRepo.save(any(Connection.class))).thenAnswer(invocation -> { 
			throw new Exception(); 
		});
		
		// Adding the 'Duplicate' Connection
		assertThrows(Exception.class, () -> Service.addConnection(DummyConnection2));
	}
	
	@Test
	public void testUpdateConnection() {
		Connection DummyConnection2 = new Connection();
		ConnectionId DummyConnection2Id = new ConnectionId();
		DummyConnection2Id.setUserFrom(1);
		DummyConnection2Id.setUserTo(3);
		DummyConnection2.setConnectionId(DummyConnection2Id);
		// Updated Attribute
		DummyConnection2.setDateAdded("1999-04-04 08:00:00");
		
		when(ConnectionRepo.findByUserFromAndTo(any(int.class), any(int.class))).thenReturn(DummyConnection2);
		
		// Updating the User
		assertTrue(Service.updateConnection(DummyConnection2));
	}
	
	@Test
	public void testUpdateConnectionNonValid() {
		// CASE#1 - Connection doesn't exist
		Connection DummyConnectionNonValid = new Connection();
		ConnectionId DummyConnectionNonValidId = new ConnectionId();
		DummyConnectionNonValidId.setUserFrom(3);
		DummyConnectionNonValidId.setUserTo(2);
		DummyConnectionNonValid.setConnectionId(DummyConnectionNonValidId);
		// Updated Attribute
		DummyConnectionNonValid.setDateAdded("1999-04-04 08:00:00");
		
		when(ConnectionRepo.findByUserFromAndTo(any(int.class), any(int.class))).thenReturn(null);
		
		// Updating the Non-Existant the Connection
		assertFalse(Service.updateConnection(DummyConnectionNonValid));
	}
	
	@Test
	public void testDeleteConnection() {
		// Deleting the Connection
		assertTrue(Service.deleteConnection(99, 99));
	}
}
