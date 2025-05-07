package com.openclassrooms.PayMyBuddy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.openclassrooms.PayMyBuddy.model.Transaction;
import com.openclassrooms.PayMyBuddy.model.TransactionDataDashboardDTO;
import com.openclassrooms.PayMyBuddy.model.UserDataFromConnectionDTO;
import com.openclassrooms.PayMyBuddy.service.ConnectionService;
import com.openclassrooms.PayMyBuddy.service.TransactionService;
import com.openclassrooms.PayMyBuddy.util.TransactionAlreadyExistsException;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>TransactionController is an entity that handles HTTP Requests related to Transactions</p>
 */
@Slf4j
@Controller
public class TransactionController {
    @Autowired
	ConnectionService ConnectionService;

    @Autowired
	TransactionService TransactionService;

    /**
	 * <p>Returns a List of every Transaction Entity registered</p>
	 * @return an HTTP Response with Code 200 containing a List of every Transaction Entity registered; an empty HTTP Response with Code 500 if a problem occurred
	 */
	@GetMapping("/transaction")
	public ResponseEntity<List<Transaction>> getTransactions() {
		ResponseEntity<List<Transaction>> Response = null;
		
		try {
			Response = new ResponseEntity<>(TransactionService.getTransactions(), HttpStatus.OK);
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
			boolean Result = TransactionService.addTransaction(transaction);
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
	/*@GetMapping("/transaction/summary")
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
	}*/

	@GetMapping("/transfer")
	public String transactionView(HttpSession session, Model model) {
		if (session.getAttribute("userId") == null)
			return "redirect:/signin";

		int sessionUserId = (int) session.getAttribute("userId");
		
		Transaction transactionForm = new Transaction();
		List<TransactionDataDashboardDTO> transactionsList = TransactionService.getAllTransactionsDataOfUser(sessionUserId);
		List<UserDataFromConnectionDTO> connectionsList = ConnectionService.getUsersConnectedToUser(sessionUserId);

		model.addAttribute("transactionForm", transactionForm);
		model.addAttribute("transactionsList", transactionsList);
		model.addAttribute("connectionsList", connectionsList);

		return "transfer";
	}

	@PostMapping("/initiateTransaction")
	public String transactionProcessView(HttpSession session, @ModelAttribute("transactionForm") Transaction transaction, Model model) {
		if (session.getAttribute("userId") == null)
			return "redirect:/signin";

		int sessionUserId = (int) session.getAttribute("userId");
		
		if (transaction.getReceiver() == -1)
			return "redirect:/transfer";
			
		transaction.setSender(sessionUserId);
		TransactionService.addTransaction(transaction);

		return "redirect:/transfer";
	}
}
