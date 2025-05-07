package com.openclassrooms.PayMyBuddy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * <p>UserController is an entity that handles HTTP Requests related to Users</p>
 */
@Slf4j
@Controller
public class UserController {
	@Autowired
	UserService UserService;
	
	/**
	 * <p>Returns a List of every User Entity registered</p>
	 * @return an HTTP Response with Code 200 containing a List of every User Entity registered; an empty HTTP Response with Code 500 if a problem occurred
	 */
	@GetMapping("/user")
	public ResponseEntity<List<User>> getUsers() {
		ResponseEntity<List<User>> Response = null;
		
		try {
			Response = new ResponseEntity<>(UserService.getUsers(), HttpStatus.OK);
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
			boolean Result = UserService.addUser(user);
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
			boolean Result = UserService.updateUser(user);
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
			boolean Result = UserService.deleteUser(username);
			if (Result == false)
				throw new Exception("Could not delete User");
				
			log.info("[DELETE] /user - " + Response.getStatusCode());
		} catch (Exception e) {
			Response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[DELETE] /user - " + Response.getStatusCode() + " (" + e + ")");
		}
		
		return Response;
	}

	@GetMapping("/signup")
    public String signUpView(Model model) {
		User userRegisterForm = new User();

		model.addAttribute("userRegisterForm", userRegisterForm);

        return "signup";
    }

    @PostMapping("/signup/save")
    public String saveUserView(@ModelAttribute("userRegisterForm") User user, Model model) {
		try {
			boolean Result = UserService.addUser(user);
			if (Result == false)
				throw new Exception("Could not register user.");

			return "signup-success";
		} catch (DataIntegrityViolationException e) {
			model.addAttribute("userRegisterForm", new User());

			log.error("/signup/save - 500");

			return "signup-error";
		} catch (Exception e) {
			model.addAttribute("userRegisterForm", new User());

			log.error("/signup/save - 500: ", e);
			
			return "signup-error";
		}
    }

	@GetMapping({"/", "/signin"})
	public String signInView(HttpSession session, Model model) {
		User userLoginForm = new User();

		model.addAttribute("userLoginForm", userLoginForm);

		return "signin";
	}

	@PostMapping("/signin")
    public String verifyUserView(HttpSession session, @ModelAttribute("userLoginForm") User user, Model model) {
		if (UserService.verifyUser(user.getEmail(), user.getPassword()) == false) 
			return "signin-error";

		User UserConnected = UserService.getUserByEmail(user.getEmail());
		session.setAttribute("userId", UserConnected.getId());
		
        return "redirect:/transfer";
    }

	@GetMapping("/signout")
	public String signOutView(HttpSession session, Model model) {
		session.invalidate();

		return "redirect:/signin";
	}

	@GetMapping("/profile")
	public String profileView(HttpSession session, Model model) {
		if (session.getAttribute("userId") == null)
			return "redirect:/signin";

		int sessionUserId = (int) session.getAttribute("userId");
		
		User userCurrentData = UserService.getUserById(sessionUserId);
		User userUpdateForm = new User();

		model.addAttribute("userCurrentData", userCurrentData);
		model.addAttribute("userUpdateForm", userUpdateForm);

		return "profile";
	}

	@PostMapping("/updateProfile")
    public String updateProfile(HttpSession session, @ModelAttribute("userUpdateForm") User user, Model model) {
		if (session.getAttribute("userId") == null)
			return "redirect:/signin";

		int sessionUserId = (int) session.getAttribute("userId");
		
		try {
			user.setId(sessionUserId);
			if (UserService.updateUser(user) == false)
				throw new Exception("Could not modify User");

			log.info("/signup/save - 200");
			return "redirect:/profile";
		} catch (Exception e) {
			log.error("/signup/save - 500: ", e);
			return "redirect:/profile";
		}
    }
}
