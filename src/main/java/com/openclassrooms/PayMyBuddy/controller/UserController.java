package com.openclassrooms.PayMyBuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>UserController is an entity that handles HTTP Requests related to Users</p>
 */
@Slf4j
@Controller
public class UserController {
	@Autowired
	UserService UserService;

	/**
	 * <p>Will display the View to 'Sign Up' into the App (using a username, an email and a password)</p>
	 * @param model the Model Entity used to fill in the template we return: userRegisterForm-a blank User Entity used to register the Client
	 * @return the 'signup' template
	 */
	@GetMapping("/signup")
    public String signUpView(Model model) {
		try {
			User userRegisterForm = new User();

			model.addAttribute("userRegisterForm", userRegisterForm);

			log.info("[GET] '/signup' -> signup");
			return "signup";
		} catch (Exception e) {
			log.info("[GET] '/signup' -> generic-error");
			log.debug(e.toString());
			return "generic-error";
		}
    }

	/**
	 * <p>Will process the register request by attempting to add the User into the App</p>
	 * @param user the 'userRegisterForm' parsed from the Model--used to register the User
	 * @param model the Model Entity used to fill in the template we return if needed
	 * @return the 'signup-success' template if everything went right; the 'signup-error' template if something went wrong during the registering process
	 */
    @PostMapping("/signup/save")
    public String saveUserView(@ModelAttribute("userRegisterForm") User user, Model model) {
		try {
			if (UserService.addUser(user) == false)
				throw new Exception("Could not add User");

			log.info("[POST] '/signup/save' -> signup-success");
			return "signup-success";
		} catch (DataIntegrityViolationException e) {
			model.addAttribute("userRegisterForm", new User());

			log.info("[POST] '/signup/save' -> signup-error");
			log.debug(e.toString());
			return "signup-error";
		} catch (Exception e) {
			log.info("[POST] '/signup/save' -> generic-error");
			log.debug(e.toString());
			return "generic-error";
		}
    }

	/**
	 * <p>Will display the View to 'Log In' (using an email and a password)</p>
	 * @param session the HttpSession Entity used to determine whether the Client is logged in or not
	 * @param model the Model Entity used to fill in the template we return: userLoginForm-the form the Client fills to log in
	 * @return the 'signin' template
	 */
	@GetMapping({"/", "/signin"})
	public String signInView(HttpSession session, Model model) {
		try {
			User userLoginForm = new User();

			model.addAttribute("userLoginForm", userLoginForm);

			log.info("[GET] '/signin' -> signup");
			return "signin";
		} catch (Exception e) {
			log.info("[GET] '/signin' -> generic-error");
			log.debug(e.toString());
			return "generic-error";
		}
	}

	/**
	 * <p>Will process the log in request by verifying the info received (email and password)</p>
	 * @param session the HttpSession Entity used to setup the session with the registered User'd Id--used throughout the App
	 * @param user the 'userLoginForm' 
	 * @param model the Model Entity used to fill in the template we return if needed
	 * @return a redirection towards the 'Transactions' View
	 */
	@PostMapping("/signin")
    public String verifyUserView(HttpSession session, @ModelAttribute("userLoginForm") User user, Model model) {
		try {
			if (UserService.verifyUser(user.getEmail(), user.getPassword()) == false) {
				log.info("[POST] '/signin' -> signin-error");
				return "signin-error";
			}

			User UserConnected = UserService.getUserByEmail(user.getEmail());
			session.setAttribute("userId", UserConnected.getId());
			
			log.info("[POST] '/signin' => transfer");
			return "redirect:/transfer";
		} catch (Exception e) {
			log.info("[POST] '/signin' -> generic-error");
			log.debug(e.toString());
			return "generic-error";
		}
    }

	/**
	 * <p>Will process the log out request by invalidating the HTTP Session</p>
	 * @param session the HttpSession Entity to invalidate
	 * @param model the Model Entity used to fill in the template we return if needed
	 * @return a redirection towards the 'Log In' View
	 */
	@GetMapping("/signout")
	public String signOutView(HttpSession session, Model model) {
		try {
			session.invalidate();

			log.info("[GET] '/signout' => signin");
			return "redirect:/signin";
		} catch (Exception e) {
			log.info("[GET] '/signout' -> generic-error");
			log.debug(e.toString());
			return "generic-error";
		}
	}

	/**
	 * <p>Will display a View for the Client's 'Profile', which they will be able to use to modify some of their data (username, email and/or password)</p>
	 * @param session the HttpSession Entity used to determine whether the Client is logged in or not
	 * @param model the Model Entity used to fill in the template we return: userCurrentData-a User Entity filled with the Client's current data; userUpdateForm a blank User Entity whose attributes will be used to update the Client's User Entity
	 * @return the 'profile' template; a redirection towards the 'Log In' View if the Client is not logged in
	 */
	@GetMapping("/profile")
	public String profileView(HttpSession session, Model model) {
		if (session.getAttribute("userId") == null) {
			log.info("[GET] '/profile' => signin");
			return "redirect:/signin";
		}

		try {
			int sessionUserId = (int) session.getAttribute("userId");
			
			User userCurrentData = UserService.getUserById(sessionUserId);
			User userUpdateForm = new User();

			model.addAttribute("userCurrentData", userCurrentData);
			model.addAttribute("userUpdateForm", userUpdateForm);

			log.info("[GET] '/profile' -> profile");
			return "profile";
		} catch (Exception e) {
			log.info("[GET] '/profile' -> generic-error");
			log.debug(e.toString());
			return "generic-error";
		}
	}

	/**
	 * <p>Will process the updating profile request; will then redirect to the 'Profile' View</p>
	 * @param session the HttpSession Entity used to determine whether the Client is logged in or not
	 * @param user the 'userUpdateForm' parsed from the Model--used to see which attributes to update within the Client's User Entity
	 * @param model the Model Entity used to fill in the template we return if needed
	 * @return a redirection to the 'Profile' View; a redirection towards the 'Log In' View if the Client is not logged in
	 */
	@PostMapping("/updateProfile")
    public String updateProfile(HttpSession session, @ModelAttribute("userUpdateForm") User user, Model model) {
		if (session.getAttribute("userId") == null) {
			log.info("[POST] '/updateProfile' => signin");
			return "redirect:/signin";
		}

		try {
			int sessionUserId = (int) session.getAttribute("userId");

			user.setId(sessionUserId);
			if (UserService.updateUser(user) == false)
				throw new Exception("Could not modify User");

			log.info("[POST] '/updateProfile' => profile");
			return "redirect:/profile";
		} catch (Exception e) {
			log.info("[POST] '/updateProfile' -> generic-error");
			log.debug(e.toString());
			return "generic-error";
		}
    }
}
