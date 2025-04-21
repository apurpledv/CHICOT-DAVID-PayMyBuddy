package com.openclassrooms.PayMyBuddy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.openclassrooms.PayMyBuddy.model.Transaction;
import com.openclassrooms.PayMyBuddy.repository.TransactionRepository;

@SpringBootTest
public class TransactionRepositoryTest {
    @Autowired
    TransactionRepository TransactionRepo;

    private Transaction DummyTransaction;

    @BeforeEach
	void setupTests() throws Exception {
		DummyTransaction = new Transaction();
		DummyTransaction.setSender(1);
        DummyTransaction.setReceiver(2);
        DummyTransaction.setDescription("null");
        DummyTransaction.setAmount(999999.99);
	}
	
	@Test
	public void testGetAllTransactions() {
		assertFalse(TransactionRepo.findAll().isEmpty());
	}

    @Test
	public void testGetAllTransactionsBySender() {
		assertFalse(TransactionRepo.findBySender(1).isEmpty());
	}
	
	@Test
	public void testGetAllTransactionsByReceiver() {
		assertFalse(TransactionRepo.findByReceiver(3).isEmpty());
	}

    @Test
	public void testGetAllTransactionsBySenderAndReceiver() {
		assertFalse(TransactionRepo.findBySenderAndReceiver(1, 2).isEmpty());
	}
	
	@Test
	public void testGetTransactionById() {
		assertTrue(TransactionRepo.findById(2) != null);
	}
	
	@Test
	public void testAddAndDeleteTransaction() {
		// Adding
		TransactionRepo.addTransaction(DummyTransaction.getSender(), DummyTransaction.getReceiver(), DummyTransaction.getDescription(), DummyTransaction.getAmount());
		assertEquals(3, TransactionRepo.count());
		
		// Deleting (Default Deletion Method)
		TransactionRepo.deleteByAmount(999999.99);
		assertEquals(2, TransactionRepo.count());
	}
}
