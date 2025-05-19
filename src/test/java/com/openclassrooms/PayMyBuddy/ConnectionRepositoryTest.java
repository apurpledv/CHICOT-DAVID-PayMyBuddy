package com.openclassrooms.PayMyBuddy;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.openclassrooms.PayMyBuddy.model.Connection;
import com.openclassrooms.PayMyBuddy.model.ConnectionId;
import com.openclassrooms.PayMyBuddy.repository.ConnectionRepository;

@SpringBootTest
@Sql(scripts = "scripts/clean.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "scripts/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "scripts/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "scripts/data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
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
	public void testAddConnection() {
		// Adding
		ConnectionRepo.save(DummyConnection);
		assertTrue(ConnectionRepo.findByUserFromAndTo(2, 3) != null);
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
