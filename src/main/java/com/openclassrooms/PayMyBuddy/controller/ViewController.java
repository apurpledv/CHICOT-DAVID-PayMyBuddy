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
import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.model.UserDataFromConnectionDTO;
import com.openclassrooms.PayMyBuddy.service.ViewService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>ViewController is an entity that returns Views to the client</p>
 */
@Slf4j
@Controller
public class ViewController {
	@Autowired
	ViewService Service;

	private int sessionUserId = 1;

	@GetMapping("/signup")
    public String signUpView(Model model) {
		User userForm = new User();

		model.addAttribute("userForm", userForm);

        return "signup";
    }

    @PostMapping("/signup/save")
    public String saveUserView(@ModelAttribute("userForm") User user, Model model) {
		if (Service.registerUser(user) == false)
			return "signup-error";

        return "signup-success";
    }

	@GetMapping({"/", "/signin"})
	public String signInView(Model model) {
		User userForm = new User();

		model.addAttribute("userForm", userForm);

		return "signin";
	}

	@PostMapping("/signin")
    public String verifyUserView(@ModelAttribute("userForm") User user, Model model) {
		// Email or Password is wrong, redirect to the same page
		if (Service.verifyUser(user) == false)
			return "signin-error";

        return "redirect:/transfer";
    }

	@GetMapping("/transfer")
	public String transferView(Model model) {
		Transaction transactionForm = new Transaction();
		List<TransactionDataDashboardDTO> transactionsList = Service.getTransactionsList(sessionUserId);
		List<UserDataFromConnectionDTO> connectionsList = Service.getUserConnections(sessionUserId);

		model.addAttribute("transactionForm", transactionForm);
		model.addAttribute("transactionsList", transactionsList);
		model.addAttribute("connectionsList", connectionsList);

		return "transfer";
	}

	@PostMapping("/initiateTransaction")
	public String transferProcessView(@ModelAttribute("transactionForm") Transaction transaction, Model model) {
		transaction.setSender(sessionUserId);
		Service.sendTransaction(transaction);

		return "redirect:/transfer";
	}
}
