package com.openclassrooms.PayMyBuddy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import com.openclassrooms.PayMyBuddy.model.Transaction;
import com.openclassrooms.PayMyBuddy.repository.TransactionRepository;

@SpringBootTest
@Sql(scripts = "scripts/clean.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "scripts/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "scripts/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "scripts/data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
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
	public void testGetAllTransactionsBelongingToUser() {
		assertFalse(TransactionRepo.findAllTransactionsBelongingToUser(1).isEmpty());
	}
	
	@Test
	public void testGetTransactionById() {
		assertTrue(TransactionRepo.getById(2) instanceof Transaction);
	}
	
	@Test
	public void testAddTransaction() {
		long InitialRepoSize = TransactionRepo.count();

		// Adding
		TransactionRepo.addTransaction(DummyTransaction.getSender(), DummyTransaction.getReceiver(), DummyTransaction.getDescription(), DummyTransaction.getAmount());
		assertEquals(InitialRepoSize + 1, TransactionRepo.count());
	}
}
