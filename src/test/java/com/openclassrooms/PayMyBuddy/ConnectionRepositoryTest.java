package com.openclassrooms.PayMyBuddy;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.PayMyBuddy.model.Connection;
import com.openclassrooms.PayMyBuddy.model.ConnectionIdentifier;
import com.openclassrooms.PayMyBuddy.repository.ConnectionRepository;

@SpringBootTest
public class ConnectionRepositoryTest {
	@Autowired
	ConnectionRepository ConnectionRepo;
	
	private Connection DummyConnection;
	
	@BeforeEach
	void setupTests() throws Exception {
		DummyConnection = new Connection();
		ConnectionIdentifier DummyConnectionId = new ConnectionIdentifier();
		DummyConnectionId.setUserFrom(2);
		DummyConnectionId.setUserTo(3);
		DummyConnection.setConnectionId(DummyConnectionId);
		DummyConnection.setDateAdded("2025-04-04 08:00:00");
	}
	
	@Test
	public void testGetAllUsers() {
		assertTrue(ConnectionRepo.findAll().size() > 0);
	}
}
