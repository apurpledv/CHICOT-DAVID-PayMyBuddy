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
