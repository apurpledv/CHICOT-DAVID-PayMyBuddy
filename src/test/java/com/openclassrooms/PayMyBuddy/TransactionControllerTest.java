package com.openclassrooms.PayMyBuddy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.PayMyBuddy.controller.TransactionController;
import com.openclassrooms.PayMyBuddy.model.Transaction;
import com.openclassrooms.PayMyBuddy.service.TransactionService;
import com.openclassrooms.PayMyBuddy.util.TransactionAlreadyExistsException;
import com.openclassrooms.PayMyBuddy.util.PMBUtil;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {
    @Autowired
	private MockMvc mockMvc;
	
	@Autowired
	TransactionController Controller;
	
	@MockitoBean
	TransactionService TransactionService;
	
	@BeforeEach
	void setupTests() {
		when(TransactionService.getTransactions()).thenReturn(new ArrayList<Transaction>());
	}
	
	@Test
	public void testGetConnections() throws Exception {
		this.mockMvc.perform(get("/transaction"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON));

		verify(TransactionService, Mockito.times(1)).getTransactions();
	}
	
	@Test
	public void testGetTransactionsNonValid() throws Exception {
		// CASE#1 - Generic Exception Thrown
		when(TransactionService.getTransactions()).thenAnswer(invocation -> { 
			throw new Exception(); 
		});
		
		this.mockMvc.perform(get("/transaction"))
			.andExpect(status().isInternalServerError());
	}
	
	@Test
	public void testAddTransaction() throws Exception {
		when(TransactionService.addTransaction(any(Transaction.class))).thenReturn(true);
		
		String Body = "{\"sender\": 1, \"receiver\": 2, \"description\": \"No details.\", \"amount\": 19.99, \"dateTransaction\": \"2025-01-01 08:00:00\"}";
		this.mockMvc.perform(post("/transaction")
			.contentType(PMBUtil.APPLICATION_JSON_UTF8)
			.content(Body)
		).andExpect(status().isOk());

		verify(TransactionService, Mockito.times(1)).addTransaction(any(Transaction.class));
	}
	
	@Test
	public void testAddTransactionNonValid() throws Exception {
		String Body = "{\"sender\": 1, \"receiver\": 2, \"description\": \"No details.\", \"amount\": 19.99, \"dateTransaction\": \"2025-01-01 08:00:00\"}";
		
		// CASE#1 - Couldn't add
		when(TransactionService.addTransaction(any(Transaction.class))).thenReturn(false);
		
		this.mockMvc.perform(post("/transaction")
			.contentType(PMBUtil.APPLICATION_JSON_UTF8)
			.content(Body)
		).andExpect(status().isInternalServerError());
		
		// CASE#2 - TransactionAlreadyExistsException is thrown
		when(TransactionService.addTransaction(any(Transaction.class))).thenAnswer(invocation -> { 
			throw new TransactionAlreadyExistsException(); 
		});
		
		this.mockMvc.perform(post("/transaction")
			.contentType(PMBUtil.APPLICATION_JSON_UTF8)
			.content(Body)
		).andExpect(status().isInternalServerError());
		
		// CASE#3 - Generic Exception Thrown
		when(TransactionService.addTransaction(any(Transaction.class))).thenAnswer(invocation -> { 
			throw new Exception(); 
		});
		
		this.mockMvc.perform(post("/transaction")
			.contentType(PMBUtil.APPLICATION_JSON_UTF8)
			.content(Body)
		).andExpect(status().isInternalServerError());
	}
}
