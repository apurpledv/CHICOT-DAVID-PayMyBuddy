package com.openclassrooms.PayMyBuddy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.openclassrooms.PayMyBuddy.model.Connection;
import com.openclassrooms.PayMyBuddy.model.ConnectionId;
import com.openclassrooms.PayMyBuddy.repository.ConnectionRepository;

@SpringBootTest
public class ConnectionRepositoryTest {
	@Autowired
	ConnectionRepository ConnectionRepo;
	
	private Connection DummyConnection;
	
	@BeforeEach
	void setupTests() throws Exception {
		DummyConnection = new Connection();
		ConnectionId DummyConnectionId = new ConnectionId();
		DummyConnectionId.setUserFrom(2);
		DummyConnectionId.setUserTo(3);
		DummyConnection.setConnectionId(DummyConnectionId);
		DummyConnection.setDateAdded("2025-04-04 08:00:00");
	}
	
	@Test
	public void testGetAllConnections() {
		assertFalse(ConnectionRepo.findAll().isEmpty());
	}
	
	@Test
	public void testGetAllConnectionsFromUser() {
		assertFalse(ConnectionRepo.findByUserFrom(1).isEmpty());
	}
	
	@Test
	public void testGetAllConnectionsToUser() {
		assertFalse(ConnectionRepo.findByUserTo(1).isEmpty());
	}
	
	@Test
	public void testGetConnectionByUserFromAndTo() {
		assertFalse(ConnectionRepo.findByUserFromAndTo(1, 2) == null);
	}
	
	@Test
	public void testGetConnectionsByDateAdded() {
		assertFalse(ConnectionRepo.findByDateAdded("2025-01-09 09:10:00").isEmpty());
	}
	
	@Test
	public void testAddAndDeleteConnection() {
		// Adding
		ConnectionRepo.save(DummyConnection);
		assertTrue(ConnectionRepo.findByUserFromAndTo(2, 3) != null);
		
		// Deleting (Default Deletion Method)
		ConnectionRepo.delete(DummyConnection);
		assertTrue(ConnectionRepo.findByUserFromAndTo(2, 3) == null);
	}
	
	@Test
	public void testUpdateConnection() {
		// Adding
		ConnectionRepo.save(DummyConnection);
		assertEquals("2025-04-04 08:00:00", ConnectionRepo.findByUserFromAndTo(2, 3).getDateAdded());
		
		// Updating
		DummyConnection.setDateAdded("1999-12-31 12:00:00");
		ConnectionRepo.save(DummyConnection);
		assertEquals("1999-12-31 12:00:00", ConnectionRepo.findByUserFromAndTo(2, 3).getDateAdded());
		
		// Clean Up
		ConnectionRepo.delete(DummyConnection);
		assertTrue(ConnectionRepo.findByUserFromAndTo(2, 3) == null);
	}
	
	@Test
	@Transactional
	public void testDeleteSpecificConnection() {
		// Adding
		ConnectionRepo.save(DummyConnection);
		assertTrue(ConnectionRepo.findByUserFromAndTo(2, 3) != null);
		
		// Deleting (Custom Deletion Method: Using UserFrom & UserTo)
		ConnectionRepo.deleteByUserFromAndTo(2, 3);
		assertTrue(ConnectionRepo.findByUserFromAndTo(2, 3) == null);
	}
}
