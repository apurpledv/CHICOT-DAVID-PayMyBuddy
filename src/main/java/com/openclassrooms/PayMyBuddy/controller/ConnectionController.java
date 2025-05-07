package com.openclassrooms.PayMyBuddy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.openclassrooms.PayMyBuddy.model.Connection;
import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.model.UserDataFromConnectionDTO;
import com.openclassrooms.PayMyBuddy.service.ConnectionService;
import com.openclassrooms.PayMyBuddy.util.ConnectionAlreadyExistsException;

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
	 * <p>Returns a List of every Connection Entity registered</p>
	 * @return an HTTP Response with Code 200 containing a List of every Connection Entity registered; an empty HTTP Response with Code 500 if a problem occurred
	 */
	@GetMapping("/connection")
	public ResponseEntity<List<Connection>> getConnections() {
		ResponseEntity<List<Connection>> Response = null;
		
		try {
			Response = new ResponseEntity<>(ConnectionService.getConnections(), HttpStatus.OK);
			log.info("[GET] /connection - " + Response.getStatusCode());
		} catch (Exception e) {
			Response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[GET] /connection - " + Response.getStatusCode() + " (" + e + ")");
		}
		
		return Response;
	}
	
	/**
	 * <p>Adds a Connection between two Users into the App</p>
	 * @param user a Connection Entity to add
	 * @return an HTTP Response with Code 200; an HTTP Response with Code 500 if a problem occurred
	 */
	@PostMapping("/connection")
	public ResponseEntity<HttpStatus> addConnection(@RequestBody Connection connection) {
		ResponseEntity<HttpStatus> Response = new ResponseEntity<>(HttpStatus.OK);
		
		try {
			boolean Result = ConnectionService.addConnection(connection);
			if (Result == false)
				throw new Exception("Could not add Connection");
				
			log.info("[POST] /connection - " + Response.getStatusCode());
		} catch (ConnectionAlreadyExistsException e) {
			Response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[POST] /connection - " + Response.getStatusCode() + " (" + e + ")");
		} catch (Exception e) {
			Response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[POST] /connection - " + Response.getStatusCode() + " (" + e + ")");
		}
		
		return Response;
	}
	
	/**
	 * <p>Deletes an existing Connection</p>
	 * @param userFromId the Id of the User the Connection stems from
	 * @param userToId the Id of the User the Connection is linked towards
	 * @return an HTTP Response with Code 200; an HTTP Response with Code 400 if the arguments are wrong; an HTTP Response with Code 500 if another problem occurred
	 */
	@DeleteMapping("/connection")
	public ResponseEntity<HttpStatus> deleteConnection(@RequestParam int userFromId, @RequestParam int userToId) {
		ResponseEntity<HttpStatus> Response = new ResponseEntity<>(HttpStatus.OK);
		
		try {
			boolean Result = ConnectionService.deleteConnection(userFromId, userToId);
			if (Result == false)
				throw new Exception("Could not delete Connection");
				
			log.info("[DELETE] /connection - " + Response.getStatusCode());
		} catch (Exception e) {
			Response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[DELETE] /connection - " + Response.getStatusCode() + " (" + e + ")");
		}
		
		return Response;
	}

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
