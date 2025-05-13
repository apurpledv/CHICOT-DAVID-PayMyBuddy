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
	
	/**
	 * <p>Will display the View for 'Relations' (to add another User & display who the Client is connected to)</p>
	 * @param session the HttpSession Entity used to determine whether the Client is logged in or not
	 * @param model the Model Entity used to fill in the template we return: connectionForm-a blank User used to create a new Connection (using the email); connectionsList-list of every User the Client is connected to 
	 * @return the 'relation' template; a redirection towards the 'Log In' View if the Client is not logged in
	 */
	@GetMapping("/relation")
	public String connectionView(HttpSession session, Model model) {
		if (session.getAttribute("userId") == null) {
			log.info("[GET] '/relation' => signin");
			return "redirect:/signin";
		}

		try {
			int sessionUserId = (int) session.getAttribute("userId");

			User connectionForm = new User();
			List<UserDataFromConnectionDTO> connectionsList = ConnectionService.getUsersConnectedToUser(sessionUserId);

			model.addAttribute("connectionForm", connectionForm);
			model.addAttribute("connectionsList", connectionsList);

			log.info("[GET] '/relation' -> relation");
			return "relation";
		} catch (Exception e) {
			log.info("[GET] '/relation' -> generic-error");
			log.debug(e.toString());
			return "generic-error";
		}
	}

	/**
	 * <p>Will create a new Connection using the email entered in the 'connectionForm'; then will redirect to the same View</p>
	 * @param session the HttpSession Entity used to determine whether the Client is logged in or not
	 * @param userData the 'connectionForm' parsed from the Model--used to retrieve the email to look for the User the Client wishes to add
	 * @param model the Model Entity used to fill in the template we return if needed
	 * @return a redirection towards the 'Relations' View; a redirection towards the 'Log In' View if the Client is not logged in
	 */
	@PostMapping("/createRelation")
	public String connectionProcessView(HttpSession session, @ModelAttribute("connectionForm") User userData, Model model) {
		if (session.getAttribute("userId") == null) {
			log.info("[POST] '/createRelation' => signin");
			return "redirect:/signin";
		}
		
		try {
			int sessionUserId = (int) session.getAttribute("userId");
		
			Connection Con = ConnectionService.createConnectionEntityFromEmail(sessionUserId, userData.getEmail());
			if (Con == null) {
				log.info("[POST] '/createRelation' => relation");
				return "redirect:/relation";
			}

			if (ConnectionService.addConnection(Con) == false) {
				log.info("[POST] '/createRelation' => relation");
				return "redirect:/relation";
			}

			log.info("[POST] '/createRelation' => relation");
			return "redirect:/relation";
		} catch (Exception e) {
			log.info("[POST] '/createRelation' -> generic-error");
			log.debug(e.toString());
			return "generic-error";
		}
	}
}
