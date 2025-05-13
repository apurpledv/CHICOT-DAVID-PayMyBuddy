package com.openclassrooms.PayMyBuddy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.openclassrooms.PayMyBuddy.model.Transaction;
import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.repository.TransactionRepository;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;
import com.openclassrooms.PayMyBuddy.service.TransactionService;

@SpringBootTest
public class TransactionServiceTest {
    @Autowired
	TransactionService Service;
	
	@MockitoBean
	UserRepository UserRepo;

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
	public void testGetTransactionById() {
		when(TransactionRepo.getById(any(int.class))).thenReturn(new Transaction());
		assertTrue(Service.getTransactionById(1) instanceof Transaction);
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
		// CASE#1 - Generic Exception
		when(TransactionRepo.findBySender(any(int.class))).thenReturn(null);
		when(TransactionRepo.addTransaction(any(int.class), any(int.class), any(String.class), any(double.class))).thenAnswer(invocation -> { 
			throw new Exception(); 
		});
		
		assertThrows(Exception.class, () -> Service.addTransaction(DummyTransaction));
	}

	@Test
	public void testGetAllTransactionsDataOfUser() {
		// Context: We are User #1, the other is User #2
		List<Transaction> TransactionsListR = new ArrayList<Transaction>();
			Transaction TR1 = new Transaction();
			TR1.setSender(2);
			TR1.setReceiver(1);
			TransactionsListR.add(TR1);

			when(TransactionRepo.findByReceiver(any(int.class))).thenReturn(TransactionsListR);

		List<Transaction> TransactionsListS = new ArrayList<Transaction>();
			Transaction TS1 = new Transaction();
			TS1.setSender(1);
			TS1.setReceiver(2);
			TransactionsListS.add(TS1);

			when(TransactionRepo.findBySender(any(int.class))).thenReturn(TransactionsListS);

		User User1 = new User();
		User1.setId(1);

		User User2 = new User();
		User2.setId(2);

		when(UserRepo.getById(eq(1))).thenReturn(User1);
		when(UserRepo.getById(eq(2))).thenReturn(User2);

		assertTrue(Service.getAllTransactionsDataOfUser(1) instanceof List);
	}
}
