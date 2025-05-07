package com.openclassrooms.PayMyBuddy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.openclassrooms.PayMyBuddy.model.Transaction;
import com.openclassrooms.PayMyBuddy.model.TransactionDataDashboardDTO;
import com.openclassrooms.PayMyBuddy.model.UserDataFromConnectionDTO;
import com.openclassrooms.PayMyBuddy.service.ConnectionService;
import com.openclassrooms.PayMyBuddy.service.TransactionService;

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
