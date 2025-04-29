package com.openclassrooms.PayMyBuddy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.model.UserDataFromConnectionDTO;
import com.openclassrooms.PayMyBuddy.service.UserService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * <p>UserController is an entity that handles HTTP Requests related to Users</p>
 */
@Slf4j
@Controller
public class UserController {
	@Autowired
	UserService Service;
	
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
	public ResponseEntity<Mono<HttpStatus>> addUser(@Validated @RequestBody User user) {
		ResponseEntity<Mono<HttpStatus>> Response = new ResponseEntity<>(HttpStatus.OK);
		
		try {
			boolean Result = Service.addUser(user);
			if (Result == false)
				throw new Exception("Could not add User");
				
			log.info("[POST] /user - " + Response.getStatusCode());
		} catch (Exception e) {
			Response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[POST] /user - " + Response.getStatusCode() + " (" + e + ")");
		}
		
		return Response;
	}
	
	/**
	 * <p>Updates an existing User</p>
	 * @param user a User Entity to update
	 * @return an HTTP Response with Code 200; an HTTP Response with Code 500 if a problem occurred
	 */
	@PutMapping("/user")
	public ResponseEntity<HttpStatus> updateUser(@Validated @RequestBody User user) {
		ResponseEntity<HttpStatus> Response = new ResponseEntity<>(HttpStatus.OK);
		
		try {
			boolean Result = Service.updateUser(user);
			if (Result == false)
				throw new Exception("User not found");
				
			log.info("[PUT] /user - " + Response.getStatusCode());
		} catch (Exception e) {
			Response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[PUT] /user - " + Response.getStatusCode() + " (" + e + ")");
		}
		
		return Response;
	}
	
	/**
	 * <p>Deletes an existing User</p>
	 * @param username the Username of the User to be deleted
	 * @return an HTTP Response with Code 200; an HTTP Response with Code 400 if the arguments are wrong; an HTTP Response with Code 500 if another problem occurred
	 */
	@DeleteMapping("/user")
	public ResponseEntity<HttpStatus> deleteUser(@Validated @RequestParam String username) {
		ResponseEntity<HttpStatus> Response = new ResponseEntity<>(HttpStatus.OK);
		
		try {
			boolean Result = Service.deleteUser(username);
			if (Result == false)
				throw new Exception("Could not delete User");
				
			log.info("[DELETE] /user - " + Response.getStatusCode());
		} catch (Exception e) {
			Response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[DELETE] /user - " + Response.getStatusCode() + " (" + e + ")");
		}
		
		return Response;
	}

	/**
	 * <p>Returns a boolean indicating whether the user </p>
	 * @param email the email of the user
	 * @param password the unhashed password of the user
	 * @return an HTTP Response with Code 200 containing a List of every User Entity registered; an empty HTTP Response with Code 500 if a problem occurred
	 */
	@GetMapping("/user/verify")
	public ResponseEntity<Boolean> verifyUser(@Validated @RequestParam String email, @Validated @RequestParam String password) {
		ResponseEntity<Boolean> Response = new ResponseEntity<>(true, HttpStatus.OK);
		
		try {
			User UserToVerify = Service.getUserByEmail(email);
			if (UserToVerify == null)
				throw new Exception("User not found");

			if (Service.verifyPassword(password, UserToVerify.getPassword()) == false)
				Response = new ResponseEntity<>(false, HttpStatus.OK);

			log.info("[GET] /user/verify - " + Response.getStatusCode());
		} catch (Exception e) {
			Response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[GET] /user/verify - " + Response.getStatusCode() + " (" + e + ")");
		}
		
		return Response;
	}

	/**
	 * <p>Returns a boolean indicating whether the user attempting to connect is valid (good email & password)</p>
	 * @param email the email of the user
	 * @param password the unhashed password of the user
	 * @return an HTTP Response with Code 200 containing a List of every User Entity registered; an empty HTTP Response with Code 500 if a problem occurred
	 */
	@GetMapping("/user/connectionsFrom")
	public ResponseEntity<List<UserDataFromConnectionDTO>> getUsersConnectedToUser(@Validated @RequestParam int userId) {
		ResponseEntity<List<UserDataFromConnectionDTO>> Response = null;
		
		try {
			Response = new ResponseEntity<>(Service.getUsersConnectedToUser(userId), HttpStatus.OK);
			log.info("[GET] /user/connectionsFrom - " + Response.getStatusCode());
		} catch (Exception e) {
			Response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[GET] /user/connectionsFrom - " + Response.getStatusCode() + " (" + e + ")");
		}
		
		return Response;
	}
}
