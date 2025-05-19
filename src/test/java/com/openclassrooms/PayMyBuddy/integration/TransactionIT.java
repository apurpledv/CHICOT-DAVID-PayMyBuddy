package com.openclassrooms.PayMyBuddy.integration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.openclassrooms.PayMyBuddy.controller.TransactionController;
import com.openclassrooms.PayMyBuddy.model.Transaction;
import com.openclassrooms.PayMyBuddy.repository.TransactionRepository;
import com.openclassrooms.PayMyBuddy.service.TransactionService;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "../scripts/clean.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "../scripts/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "../scripts/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "../scripts/data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
public class TransactionIT {
    @Autowired
	private MockMvc mockMvc;
	
	@Autowired
	TransactionController Controller;
	
	@Autowired
	TransactionService Service;

	@Autowired
	TransactionRepository Repository;
	
	private HashMap<String, Object> SessionAttributes = new HashMap<String, Object>();
	
	@BeforeEach
	void setupTests() {
		SessionAttributes = new HashMap<String, Object>();
		SessionAttributes.put("userId", 1);
	}

    @Test
    public void testGetTransactionsViewIT() throws Exception {
        this.mockMvc.perform(get("/transfer")
			.sessionAttrs(SessionAttributes)
		).andExpect(status().isOk());
    }

	@Test
    public void testGetTransactionsViewITNonValid() throws Exception {
        this.mockMvc.perform(get("/transfer")
		).andExpect(status().isFound());
    }

    @Test
	@Transactional
    public void testPostTransactionsViewIT() throws Exception {
        Transaction ValidTransaction = new Transaction();
		ValidTransaction.setSender(1);
		ValidTransaction.setReceiver(2);
		ValidTransaction.setAmount(150);
		ValidTransaction.setDescription("TESTTRANSACTION");

        // Send the Request
        this.mockMvc.perform(post("/initiateTransaction")
			.sessionAttrs(SessionAttributes)
			.flashAttr("transactionForm", ValidTransaction)
		).andExpect(status().isFound());

        // Check that it has been added
        List<Transaction> TransactionsList = Service.getTransactions();
		boolean IsNewTransactionPresent = false;
		for (Transaction transaction : TransactionsList) {
			if (transaction.getSender() == 1 && transaction.getReceiver() == 2 && transaction.getAmount() == 150 && transaction.getDescription().equals("TESTTRANSACTION")) {
				IsNewTransactionPresent = true;
				break;
			}
		}

		assertTrue(IsNewTransactionPresent);
    }
}
