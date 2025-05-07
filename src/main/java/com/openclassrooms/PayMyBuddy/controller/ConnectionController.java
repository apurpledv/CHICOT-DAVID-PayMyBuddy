package com.openclassrooms.PayMyBuddy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.openclassrooms.PayMyBuddy.model.Connection;
import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.model.UserDataFromConnectionDTO;
import com.openclassrooms.PayMyBuddy.service.ConnectionService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>ConnectionController is an entity that handles HTTP Requests related to Connections</p>
 */
@Slf4j
@Controller
public class ConnectionController {
	@Autowired
	ConnectionService ConnectionService;
	
	@GetMapping("/relation")
	public String connectionView(HttpSession session, Model model) {
		if (session.getAttribute("userId") == null)
			return "redirect:/signin";

		int sessionUserId = (int) session.getAttribute("userId");

		User connectionForm = new User();
		List<UserDataFromConnectionDTO> connectionsList = ConnectionService.getUsersConnectedToUser(sessionUserId);

		model.addAttribute("connectionForm", connectionForm);
		model.addAttribute("connectionsList", connectionsList);

		return "relation";
	}

	@PostMapping("/createRelation")
	public String connectionProcessView(HttpSession session, @ModelAttribute("connectionForm") User userData, Model model) {
		if (session.getAttribute("userId") == null)
			return "redirect:/signin";

		int sessionUserId = (int) session.getAttribute("userId");
		
		Connection Con = ConnectionService.createConnectionEntityFromEmail(sessionUserId, userData.getEmail());
		if (Con == null)
			return "redirect:/relation";

		if (ConnectionService.addConnection(Con) == false)
			return "redirect:/relation";

		return "redirect:/relation";
	}
}
