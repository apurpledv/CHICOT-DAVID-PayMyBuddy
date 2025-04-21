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

import com.openclassrooms.PayMyBuddy.model.Transaction;
import com.openclassrooms.PayMyBuddy.repository.TransactionRepository;
import com.openclassrooms.PayMyBuddy.service.TransactionService;
import com.openclassrooms.PayMyBuddy.util.TransactionAlreadyExistsException;

@SpringBootTest
public class TransactionServiceTest {
    @Autowired
	TransactionService Service;
	
	@MockitoBean
	TransactionRepository TransactionRepo;

    private List<Transaction> DummyList = null;
    private Transaction DummyTransaction = null;

    @BeforeEach
	public void setupTests() throws Exception {
		DummyList = new ArrayList<Transaction>();
		DummyTransaction = new Transaction();
		DummyTransaction.setSender(1);
        DummyTransaction.setReceiver(2);
        DummyTransaction.setDescription("null");
        DummyTransaction.setAmount(9.99);
        DummyTransaction.setDateTransaction("2025-04-10 15:00:00");
		DummyList.add(DummyTransaction);
		
		when(TransactionRepo.findAll()).thenReturn(DummyList);
	}

    @Test
	public void testGetAllTransactions() {
		List<Transaction> TransactionsList = Service.getTransactions();
		
		assertFalse(TransactionsList.isEmpty());
		Transaction Transaction = TransactionsList.get(0);
		assertEquals("null", Transaction.getDescription());
		assertEquals(9.99, Transaction.getAmount());
		assertEquals("2025-04-10 15:00:00", Transaction.getDateTransaction());
	}
	
	@Test
	public void testGetTransactionsBySender() {
		when(TransactionRepo.findBySender(any(int.class))).thenReturn(new ArrayList<Transaction>());
		assertTrue(Service.getTransactionsBySender(1) instanceof List);
	}
	
	@Test
	public void testGetTransactionsByReceiver() {
		when(TransactionRepo.findByReceiver(any(int.class))).thenReturn(new ArrayList<Transaction>());
		assertTrue(Service.getTransactionsByReceiver(1) instanceof List);
	}
	
	@Test
	public void testGetTransactionsBySenderAndReceiver() {
		when(TransactionRepo.findBySenderAndReceiver(any(int.class), any(int.class))).thenReturn(new ArrayList<Transaction>());
		assertTrue(Service.getTransactionsBySenderAndReceiver(1, 2) instanceof List);
	}

    @Test
	public void testGetExistingTransaction() {
		when(TransactionRepo.findExistingTransaction(any(int.class), any(int.class), any(String.class))).thenReturn(new Transaction());
		assertTrue(Service.getExistingTransaction(1, 2, "2025-04-04 15:00:00") instanceof Transaction);
	}
	
	@Test
	public void testAddTransaction() throws Exception {
		Transaction DummyTransaction2 = new Transaction();
		
		// Adding the Transaction
		assertTrue(Service.addTransaction(DummyTransaction2));
	}
	
	@Test
	public void testAddTransactionNonValid() {
        // CASE#1 - Transaction already exists
        when(TransactionRepo.findBySender(any(int.class))).thenReturn(DummyList);
        
        assertThrows(TransactionAlreadyExistsException.class, () -> Service.addTransaction(DummyTransaction));

		// CASE#2 - Generic Exception
		when(TransactionRepo.save(any(Transaction.class))).thenAnswer(invocation -> { 
			throw new Exception(); 
		});
		
		assertThrows(Exception.class, () -> Service.addTransaction(DummyTransaction));
	}
	
	@Test
	public void testUpdateTransaction() {
		assertEquals(9.99, DummyTransaction.getAmount());

        // Updating the attribute
        DummyTransaction.setAmount(99.99);
		
		when(TransactionRepo.findExistingTransaction(any(int.class), any(int.class), any(String.class))).thenReturn(DummyTransaction);
		
		// Updating the Transaction
		assertTrue(Service.updateTransaction(DummyTransaction));
	}
	
	@Test
	public void testUpdateTransactionNonValid() {
		// CASE#1 - Transaction doesn't exist
		Transaction DummyConnectionNonValid = new Transaction();
        DummyConnectionNonValid.setSender(99);
        DummyConnectionNonValid.setReceiver(999);
        DummyConnectionNonValid.setDateTransaction("1999-12-31");
		
		when(TransactionRepo.findExistingTransaction(any(int.class), any(int.class), any(String.class))).thenReturn(null);
		
		// Updating the Non-Existant the Transaction
		assertFalse(Service.updateTransaction(DummyConnectionNonValid));
	}
	
	@Test
	public void testDeleteTransaction() {
		// Deleting the Transaction
		assertTrue(Service.deleteTransaction(99));
	}
}
