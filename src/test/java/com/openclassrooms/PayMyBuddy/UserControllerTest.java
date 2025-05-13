package com.openclassrooms.PayMyBuddy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.PayMyBuddy.controller.UserController;
import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	UserController Controller;
	
	@MockitoBean
	UserService UserService;
	
	private HashMap<String, Object> SessionAttributes = new HashMap<String, Object>();
	
	@BeforeEach
	void setupTests() {
		SessionAttributes = new HashMap<String, Object>();
		SessionAttributes.put("userId", 9999);
	}

	@Test
	public void testGetSignUpView() throws Exception {
		this.mockMvc.perform(get("/signup")
		).andExpect(status().isOk());
	}

	@Test
	public void testPostSignUpView() throws Exception {
		User UserInfo = new User();

		when(UserService.addUser(any(User.class))).thenReturn(true);

		this.mockMvc.perform(post("/signup/save")
			.flashAttr("userRegisterForm", UserInfo)
		).andExpect(status().isOk());
	}

	@Test
	public void testPostSignUpViewNonValid() throws Exception {
		User UserInfo = new User();

		// CASE#1 - Error when adding the User
		when(UserService.addUser(any(User.class))).thenReturn(false);

		this.mockMvc.perform(post("/signup/save")
			.flashAttr("userRegisterForm", UserInfo)
		).andExpect(status().isOk());

		// CASE#2 - DataIntegrityViolationException (duplicate entries) thrown when adding the User
		when(UserService.addUser(any(User.class))).thenReturn(true);
		when(UserService.addUser(any(User.class))).thenAnswer(invocation -> { 
			throw new DataIntegrityViolationException(""); 
		});

		this.mockMvc.perform(post("/signup/save")
			.flashAttr("userRegisterForm", UserInfo)
		).andExpect(status().isOk());

		// CASE#3 - Generic Exception
		when(UserService.addUser(any(User.class))).thenAnswer(invocation -> { 
			throw new Exception(); 
		});

		this.mockMvc.perform(post("/signup/save")
			.flashAttr("userRegisterForm", UserInfo)
		).andExpect(status().isOk());
	}
	
	@Test
	public void testGetSignInView() throws Exception {
		this.mockMvc.perform(get("/")
		).andExpect(status().isOk());

		this.mockMvc.perform(get("/signin")
		).andExpect(status().isOk());
	}

	@Test
	public void testPostSignInView() throws Exception {
		User UserInfo = new User();
		UserInfo.setId(9999);
		UserInfo.setEmail("");
		UserInfo.setPassword("");

		when(UserService.verifyUser(any(String.class), any(String.class))).thenReturn(true);
		when(UserService.getUserByEmail(any(String.class))).thenReturn(UserInfo);

		this.mockMvc.perform(post("/signin")
			.flashAttr("userLoginForm", UserInfo)
		).andExpect(status().isFound());
	}

	@Test
	public void testPostSignInViewNonValid() throws Exception {
		User UserInfo = new User();
		UserInfo.setId(9999);
		UserInfo.setEmail("none");
		UserInfo.setPassword("none");

		// CASE#1 - Wrong email/password--User is not Verified
		when(UserService.verifyUser(any(String.class), any(String.class))).thenReturn(false);
		
		this.mockMvc.perform(post("/signin")
			.flashAttr("userLoginForm", UserInfo)
		).andExpect(status().isOk());

		// CASE#2 - Generic Exception
		when(UserService.verifyUser(any(String.class), any(String.class))).thenReturn(true);
		when(UserService.getUserByEmail(any(String.class))).thenAnswer(invocation -> { 
			throw new Exception(); 
		});

		this.mockMvc.perform(post("/signin")
			.flashAttr("userLoginForm", UserInfo)
		).andExpect(status().isOk());
	}

	@Test
	public void testGetSignOutView() throws Exception {
		this.mockMvc.perform(get("/signout")
		).andExpect(status().isFound());
	}

	@Test
	public void testGetProfileView() throws Exception {
		User UserInfo = new User();
		UserInfo.setId(9999);
		UserInfo.setEmail("");
		UserInfo.setPassword("");

		when(UserService.getUserById(any(int.class))).thenReturn(UserInfo);

		this.mockMvc.perform(get("/profile")
			.sessionAttrs(SessionAttributes)
		).andExpect(status().isOk());
	}

	@Test
	public void testGetProfileViewNonValid() throws Exception {
		User UserInfo = new User();
		UserInfo.setId(9999);
		UserInfo.setEmail("");
		UserInfo.setPassword("");

		// CASE#1 - No Session -> Redirection to Login
		this.mockMvc.perform(get("/profile")
		).andExpect(status().isFound());

		// CASE#2 - Generic Exception
		when(UserService.getUserById(any(int.class))).thenAnswer(invocation -> { 
			throw new Exception(); 
		});

		this.mockMvc.perform(get("/profile")
			.sessionAttrs(SessionAttributes)
		).andExpect(status().isOk());
	}

	@Test
	public void testPostProfileView() throws Exception {
		User UserInfo = new User();
		UserInfo.setId(9999);
		UserInfo.setEmail("");
		UserInfo.setPassword("");

		when(UserService.updateUser(any(User.class))).thenReturn(true);

		this.mockMvc.perform(post("/updateProfile")
			.sessionAttrs(SessionAttributes)
			.flashAttr("userUpdateForm", UserInfo)
		).andExpect(status().isFound());
	}

	@Test
	public void testPostProfileViewNonValid() throws Exception {
		User UserInfo = new User();
		UserInfo.setId(9999);
		UserInfo.setEmail("none");
		UserInfo.setPassword("none");

		// CASE#1 - No Session -> Redirection to Login
		this.mockMvc.perform(post("/updateProfile")
		).andExpect(status().isFound());

		// CASE#2 - Error when modifying User
		when(UserService.updateUser(any(User.class))).thenReturn(false);
		this.mockMvc.perform(post("/updateProfile")
			.sessionAttrs(SessionAttributes)
			.flashAttr("userUpdateForm", UserInfo)
		).andExpect(status().isOk());

		// CASE#3 - Generic Exception
		when(UserService.updateUser(any(User.class))).thenReturn(true);
		when(UserService.updateUser(any(User.class))).thenAnswer(invocation -> { 
			throw new Exception(); 
		});

		this.mockMvc.perform(post("/updateProfile")
			.sessionAttrs(SessionAttributes)
			.flashAttr("userUpdateForm", UserInfo)
		).andExpect(status().isOk());
	}
}
