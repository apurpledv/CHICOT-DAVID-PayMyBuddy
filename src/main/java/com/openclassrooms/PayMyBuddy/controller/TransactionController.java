package com.openclassrooms.PayMyBuddy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.openclassrooms.PayMyBuddy.model.Transaction;
import com.openclassrooms.PayMyBuddy.model.TransactionDataDashboardDTO;
import com.openclassrooms.PayMyBuddy.service.TransactionService;
import com.openclassrooms.PayMyBuddy.util.TransactionAlreadyExistsException;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>TransactionController is an entity that handles HTTP Requests related to Transactions</p>
 */
@Slf4j
@Controller
public class TransactionController {
    @Autowired
	TransactionService Service;

    /**
	 * <p>Returns a List of every Transaction Entity registered</p>
	 * @return an HTTP Response with Code 200 containing a List of every Transaction Entity registered; an empty HTTP Response with Code 500 if a problem occurred
	 */
	@GetMapping("/transaction")
	public ResponseEntity<List<Transaction>> getTransactions() {
		ResponseEntity<List<Transaction>> Response = null;
		
		try {
			Response = new ResponseEntity<>(Service.getTransactions(), HttpStatus.OK);
			log.info("[GET] /transaction - " + Response.getStatusCode());
		} catch (Exception e) {
			Response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[GET] /transaction - " + Response.getStatusCode() + " (" + e + ")");
		}
		
		return Response;
	}
	
	/**
	 * <p>Adds a Transaction into the App</p>
	 * @param user a Transaction Entity to add
	 * @return an HTTP Response with Code 200; an HTTP Response with Code 500 if a problem occurred
	 */
	@PostMapping("/transaction")
	public ResponseEntity<HttpStatus> addTransaction(@RequestBody Transaction transaction) {
		ResponseEntity<HttpStatus> Response = new ResponseEntity<>(HttpStatus.OK);
		
		try {
			boolean Result = Service.addTransaction(transaction);
			if (Result == false)
				throw new Exception("Could not add Transaction");
				
			log.info("[POST] /transaction - " + Response.getStatusCode());
		} catch (TransactionAlreadyExistsException e) {
			Response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[POST] /transaction - " + Response.getStatusCode() + " (" + e + ")");
		} catch (Exception e) {
			Response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[POST] /transaction - " + Response.getStatusCode() + " (" + e + ")");
		}
		
		return Response;
	}

	/**
	 * <p>Returns a boolean indicating whether the user attempting to connect is valid (good email & password)</p>
	 * @param email the email of the user
	 * @param password the unhashed password of the user
	 * @return an HTTP Response with Code 200 containing a List of every User Entity registered; an empty HTTP Response with Code 500 if a problem occurred
	 */
	@GetMapping("/transaction/summary")
	public ResponseEntity<List<TransactionDataDashboardDTO>> getTransactionsLinkedToUser(@Validated @RequestParam int userId) {
		ResponseEntity<List<TransactionDataDashboardDTO>> Response = null;
		
		try {
			Response = new ResponseEntity<>(Service.getAllTransactionsDataOfUser(userId), HttpStatus.OK);
			log.info("[GET] /transaction/summary - " + Response.getStatusCode());
		} catch (Exception e) {
			Response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[GET] /transaction/summary - " + Response.getStatusCode() + " (" + e + ")");
		}
		
		return Response;
	}
}
