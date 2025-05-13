package com.openclassrooms.PayMyBuddy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.PayMyBuddy.controller.TransactionController;
import com.openclassrooms.PayMyBuddy.model.Transaction;
import com.openclassrooms.PayMyBuddy.service.TransactionService;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {
    @Autowired
	private MockMvc mockMvc;
	
	@Autowired
	TransactionController Controller;
	
	@MockitoBean
	TransactionService TransactionService;
	
	private HashMap<String, Object> SessionAttributes = new HashMap<String, Object>();
	
	@BeforeEach
	void setupTests() {
		SessionAttributes = new HashMap<String, Object>();
		SessionAttributes.put("userId", 9999);
	}
	
	@Test
	public void testGetTransactionsView() throws Exception {
		this.mockMvc.perform(get("/transfer")
			.sessionAttrs(SessionAttributes)
		).andExpect(status().isOk());
	}

	@Test
	public void testGetTransactionsViewNonValid() throws Exception {
		// CASE#1 - No Session -> Redirection to Login
		this.mockMvc.perform(get("/transfer")
		).andExpect(status().isFound());

		// CASE#2 - Generic Exception
		when(TransactionService.getAllTransactionsDataOfUser(any(int.class))).thenAnswer(invocation -> { 
			throw new Exception(); 
		});

		this.mockMvc.perform(get("/transfer")
			.sessionAttrs(SessionAttributes)
		).andExpect(status().isOk());
	}

	@Test
	public void testPostTransactionsView() throws Exception {
		Transaction ValidTransaction = new Transaction();
		ValidTransaction.setReceiver(1);

		when(TransactionService.addTransaction(any(Transaction.class))).thenReturn(true);

		this.mockMvc.perform(post("/initiateTransaction")
			.sessionAttrs(SessionAttributes)
			.flashAttr("transactionForm", ValidTransaction)
		).andExpect(status().isFound());
	}

	@Test
	public void testPostTransactionsViewNonValid() throws Exception {
		Transaction ValidTransaction = new Transaction();
		ValidTransaction.setReceiver(1);

		// CASE#1 - No Session -> Redirection to Login
		this.mockMvc.perform(post("/initiateTransaction")
		).andExpect(status().isFound());

		// CASE#2 - The Receiver Id isn't valid (== -1)
		ValidTransaction.setReceiver(-1);
		this.mockMvc.perform(post("/initiateTransaction")
			.sessionAttrs(SessionAttributes)
			.flashAttr("transactionForm", ValidTransaction)
		).andExpect(status().isFound());

		// CASE#3 - A problem occurred when trying to add the Transaction Entity into the App
		ValidTransaction.setReceiver(1);
		when(TransactionService.addTransaction(any(Transaction.class))).thenReturn(false);
		
		this.mockMvc.perform(post("/initiateTransaction")
			.sessionAttrs(SessionAttributes)
			.flashAttr("transactionForm", ValidTransaction)
		).andExpect(status().isOk());

		// CASE#4 - Generic Exception
		when(TransactionService.addTransaction(any(Transaction.class))).thenAnswer(invocation -> { 
			throw new Exception(); 
		});

		this.mockMvc.perform(post("/initiateTransaction")
			.sessionAttrs(SessionAttributes)
			.flashAttr("transactionForm", ValidTransaction)
		).andExpect(status().isOk());
	}
}
