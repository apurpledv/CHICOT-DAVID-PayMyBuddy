package com.openclassrooms.PayMyBuddy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.service.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>UserController is an entity that handles HTTP Requests related to Users</p>
 */
@Slf4j
@Controller
public class UserController {
	@Autowired
	UserService Service;
	
	@GetMapping("/")
	public void home() {
		
	}
	
	/**
	 * <p>Returns a List of every User Entity registered</p>
	 * @return an HTTP Response with Code 200 containing a List of every User Entity registered; an empty HTTP Response with Code 500 if a problem occurred
	 */
	@GetMapping("/user")
	public ResponseEntity<List<User>> getUsers() {
		ResponseEntity<List<User>> Response = null;
		
		try {
			Response = new ResponseEntity<>(Service.getUsers(), HttpStatus.OK);
			log.info("[GET] /user - " + Response.getStatusCode());
		} catch (Exception e) {
			Response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[GET] /user - " + Response.getStatusCode() + " (" + e + ")");
		}
		
		return Response;
	}
	
	/**
	 * <p>Adds a User into the App</p>
	 * @param user a User Entity to add
	 * @return an HTTP Response with Code 200; an HTTP Response with Code 500 if a problem occurred
	 */
	@PostMapping("/user")
	public ResponseEntity<HttpStatus> addUser(@RequestBody User user) {
		ResponseEntity<HttpStatus> Response = new ResponseEntity<>(HttpStatus.OK);
		
		try {
			boolean Result = Service.addUser(user);
			if (Result == false)
				throw new Exception("Could not add User");
				
			log.info("[POST] /user - " + Response.getStatusCode());
		} catch (Exception e) {
			Response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.info("[POST] /user - " + Response.getStatusCode() + " (" + e + ")");
		}
		
		return Response;
	}
	
	/**
	 * <p>Updates an existing User</p>
	 * @param user a User Entity to update
	 * @return an HTTP Response with Code 200; an HTTP Response with Code 500 if a problem occurred
	 */
	@PutMapping("/user")
	public ResponseEntity<HttpStatus> updateUser(@RequestBody User user) {
		ResponseEntity<HttpStatus> Response = new ResponseEntity<>(HttpStatus.OK);
		
		try {
			boolean Result = Service.updateUser(user);
			if (Result == false)
				throw new Exception("Could not update User");
				
			log.info("[PUT] /user - " + Response.getStatusCode());
		} catch (Exception e) {
			Response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.info("[PUT] /user - " + Response.getStatusCode() + " (" + e + ")");
		}
		
		return Response;
	}
	
	/**
	 * <p>Deletes an existing User</p>
	 * @param username the Username of the User to be deleted
	 * @return an HTTP Response with Code 200; an HTTP Response with Code 400 if the arguments are wrong; an HTTP Response with Code 500 if another problem occurred
	 */
	@DeleteMapping("/user")
	public ResponseEntity<HttpStatus> deleteUser(@RequestParam String username) {
		ResponseEntity<HttpStatus> Response = new ResponseEntity<>(HttpStatus.OK);
		
		try {
			boolean Result = Service.deleteUser(username);
			if (Result == false)
				throw new Exception("Could not delete User");
				
			log.info("[DELETE] /user - " + Response.getStatusCode());
		} catch (Exception e) {
			Response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.info("[DELETE] /user - " + Response.getStatusCode() + " (" + e + ")");
		}
		
		return Response;
	}
}
