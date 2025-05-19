package com.openclassrooms.PayMyBuddy.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.PayMyBuddy.controller.UserController;
import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;
import com.openclassrooms.PayMyBuddy.service.UserService;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "../scripts/clean.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "../scripts/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "../scripts/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "../scripts/data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
public class UserIT {
    @Autowired
	private MockMvc mockMvc;

    @Autowired
    UserController Controller;

    @Autowired
    UserService Service;

    @Autowired
    UserRepository Repository;

    private HashMap<String, Object> SessionAttributes = new HashMap<String, Object>();
	
	@BeforeEach
	void setupTests() {
		SessionAttributes = new HashMap<String, Object>();
		SessionAttributes.put("userId", 2);
	}

    @Test
    public void testGetSignUpViewIT() throws Exception {
        this.mockMvc.perform(get("/signup")
		).andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testPostSignUpViewIT() throws Exception {
        User UserRegisterInfo = new User();
        UserRegisterInfo.setUser("testUser");
        UserRegisterInfo.setEmail("testEmail");
        UserRegisterInfo.setPassword("testPassword");

        this.mockMvc.perform(post("/signup/save")
            .flashAttr("userRegisterForm", UserRegisterInfo)
		).andExpect(status().isOk());

        assertTrue(Repository.findByUser("testUser") instanceof User);

        Repository.deleteByUser("testUser");
    }

    @Test
    public void testGetSignInViewIT() throws Exception {
        this.mockMvc.perform(get("/signin")
		).andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testPostSignInViewIT() throws Exception {
        User UserLoginInfo = new User();
        UserLoginInfo.setEmail("testEmail");
        UserLoginInfo.setPassword("testPassword");

        Repository.addUser("testUser", "testEmail", Service.createPassword("testPassword"));

        this.mockMvc.perform(post("/signin")
            .flashAttr("userLoginForm", UserLoginInfo)
		).andExpect(status().isFound());
    }

    @Test
    @Transactional
    public void testPostSignInViewITNonValid() throws Exception {
        User UserLoginInfo = new User();
        UserLoginInfo.setEmail("testEmail");
        UserLoginInfo.setPassword("testPassword");

        this.mockMvc.perform(post("/signin")
            .flashAttr("userLoginForm", UserLoginInfo)
		).andExpect(status().isOk());
    }

    @Test
    public void testGetSignOutViewIT() throws Exception {
        this.mockMvc.perform(get("/signout")
		).andExpect(status().isFound());
    }

    @Test
    public void testGetProfileViewIT() throws Exception {
        this.mockMvc.perform(get("/profile")
			.sessionAttrs(SessionAttributes)
		).andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testPostProfileViewIT() throws Exception {
        User UserInfo = new User();
		UserInfo.setEmail("testEmailEmail");

        Repository.addUser("testUser", "testEmail", Service.createPassword("testPassword"));

		this.mockMvc.perform(post("/updateProfile")
			.sessionAttr("userId", Repository.findByUser("testUser").getId())
			.flashAttr("userUpdateForm", UserInfo)
		).andExpect(status().isFound());

        User UpdatedUser = Repository.findByUser("testUser");
        assertEquals("testEmailEmail", UpdatedUser.getEmail());
    }

    @Test
    @Transactional
    public void testPostProfileViewITNonValid() throws Exception {
        // CASE#1 - No Session -> Redirection
        this.mockMvc.perform(post("/updateProfile")
		).andExpect(status().isFound());
    }
}
