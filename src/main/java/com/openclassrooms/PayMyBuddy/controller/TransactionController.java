package com.openclassrooms.PayMyBuddy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

	/**
	 * <p>Will display the View for 'Transactions' (with a form to create a new Transaction & a list of previous Transactions made)</p>
	 * @param session the HttpSession Entity used to determine whether the Client is logged in or not
	 * @param model the Model Entity used to fill in the template we return: transactionForm-the form to make a new Transaction; transactionsList-a list of previous Transaction Entities linked to the Client (whether he is the Sender or Receiver); connectionsList-list of Users the Client is connected to (for the Transactions Form)
	 * @return the 'transfer' template; a redirection towards the 'Log In' View if the Client is not logged in
	 */
	@GetMapping("/transfer")
	public String transactionView(HttpSession session, Model model) {
		if (session.getAttribute("userId") == null) {
			log.info("[GET] '/transfer' => signin");
			return "redirect:/signin";
		}

		try {
			int sessionUserId = (int) session.getAttribute("userId");

			Transaction transactionForm = new Transaction();
			List<TransactionDataDashboardDTO> transactionsList = TransactionService.getAllTransactionsDataOfUser(sessionUserId);
			List<UserDataFromConnectionDTO> connectionsList = ConnectionService.getUsersConnectedToUser(sessionUserId);

			model.addAttribute("transactionForm", transactionForm);
			model.addAttribute("transactionsList", transactionsList);
			model.addAttribute("connectionsList", connectionsList);

			log.info("[GET] '/transfer' -> transfer");
			return "transfer";
		} catch (Exception e) {
			log.info("[GET] '/transfer' -> generic-error");
			log.debug(e.toString());
			return "generic-error";
		}
	}

	/**
	 * <p>Will create a new Transaction based on the 'transactionsForm' filled on the 'Transactions' View; will then redirect towards the same View</p>
	 * @param session the HttpSession Entity used to determine whether the Client is logged in or not
	 * @param transaction the 'transactionForm' parsed from the Model--used to set up the Transaction, and register it
	 * @param model the Model Entity used to fill in the template we return if needed
	 * @return a redirection towards the 'Transactions' View; a redirection towards the 'Log In' View if the Client is not logged in
	 */
	@PostMapping("/initiateTransaction")
	public String transactionProcessView(HttpSession session, @ModelAttribute("transactionForm") Transaction transaction, Model model, RedirectAttributes redirectAttributes) {
		if (session.getAttribute("userId") == null) {
			log.info("[POST] '/transfer' => signin");
			return "redirect:/signin";
		}

		try {
			int sessionUserId = (int) session.getAttribute("userId");
		
			if (transaction.getReceiver() == -1) {
				redirectAttributes.addFlashAttribute("formMessage", "Veuillez sélectionner une Relation.");
				redirectAttributes.addFlashAttribute("formMessageType", "INFO");
				
				log.info("[POST] '/transfer' => transfer");
				return "redirect:/transfer";
			}
				
			transaction.setSender(sessionUserId);
			
			if (TransactionService.addTransaction(transaction) == false)
				throw new Exception("Could not add Transaction");

			redirectAttributes.addFlashAttribute("formMessage", "Votre Transaction a bien été prise en charge.");
			redirectAttributes.addFlashAttribute("formMessageType", "SUCCESS");

			log.info("[POST] '/transfer' => transfer");
			return "redirect:/transfer";
		} catch (Exception e) {
			log.info("[POST] '/transfer' -> generic-error");
			log.debug(e.toString());
			return "generic-error";
		}
	}
}
