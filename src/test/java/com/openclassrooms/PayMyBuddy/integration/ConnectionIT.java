package com.openclassrooms.PayMyBuddy.integration;

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

import com.openclassrooms.PayMyBuddy.controller.ConnectionController;
import com.openclassrooms.PayMyBuddy.model.Connection;
import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.repository.ConnectionRepository;
import com.openclassrooms.PayMyBuddy.service.ConnectionService;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "../scripts/clean.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "../scripts/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "../scripts/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "../scripts/data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
public class ConnectionIT {
    @Autowired
	private MockMvc mockMvc;

    @Autowired
    ConnectionController Controller;

    @Autowired
    ConnectionService Service;

    @Autowired
    ConnectionRepository Repository;

    private HashMap<String, Object> SessionAttributes = new HashMap<String, Object>();
	
	@BeforeEach
	void setupTests() {
		SessionAttributes = new HashMap<String, Object>();
		SessionAttributes.put("userId", 2);
	}

    @Test
    public void testGetConnectionsViewIT() throws Exception {
        this.mockMvc.perform(get("/relation")
			.sessionAttrs(SessionAttributes)
		).andExpect(status().isOk());
    }

    @Test
    public void testGetConnectionsViewITNonValid() throws Exception {
        this.mockMvc.perform(get("/relation")
		).andExpect(status().isFound());
    }

    @Test
    @Transactional
    public void testPostConnectionsViewIT() throws Exception {
        User UserEmail = new User();
        UserEmail.setEmail("wongwong@gmail.com");

        // Send the Request
        this.mockMvc.perform(post("/createRelation")
			.sessionAttrs(SessionAttributes)
            .flashAttr("connectionForm", UserEmail)
		).andExpect(status().isFound());

        // Check that it has been added
        assertTrue(Service.getConnectionBetweenUsers(2, 3) instanceof Connection);
    }

    @Test
    @Transactional
    public void testPostConnectionsViewITNonValid() throws Exception {
        // CASE#1 - No Session -> Redirection
        this.mockMvc.perform(post("/createRelation")
		).andExpect(status().isFound());

        // CASE#2 - No Email -> Exception
        this.mockMvc.perform(post("/createRelation")
			.sessionAttrs(SessionAttributes)
		).andExpect(status().isFound());
    }

}
