package com.example.healthyshop.web.controllers;

import com.example.healthyshop.model.Food;
import com.example.healthyshop.model.User;
import com.example.healthyshop.util.TestDataUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils testDataUtils;

    private User testUser;
    private User testAdmin;
    private Food testFood;

    @BeforeEach
    void setUp() {
        testUser = testDataUtils.createTestUser("user");
        testAdmin = testDataUtils.createTestAdmin("admin");
        testFood = testDataUtils.createTestFood();
    }

    @AfterEach
    void tearDown() {
        testDataUtils.cleanUpDatabase();
    }

    @Test
    void testLoginPageShown() throws Exception {
        mockMvc.perform(get("/users/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void testOnFailedLogin() throws Exception {
        mockMvc.perform(post("/users/login-error"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testViewProfilesByUser_Forbidden() throws Exception {
        mockMvc.perform(get("/profiles"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void testViewProfilesByAdmin() throws Exception {
        mockMvc.perform(get("/profiles"))
                .andExpect(status().isOk())
                .andExpect(view().name("profiles"));
    }

    @Test
    void testMakeUserAdminByAnonymous_Forbidden() throws Exception {
        mockMvc.perform(get("/make/admin/{id}", testUser.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testMakeUserAdminByUser_Forbidden() throws Exception {
        mockMvc.perform(get("/make/admin/{id}", testUser.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void testMakeUserAdminByAdmin() throws Exception {
        mockMvc.perform(get("/make/admin/{id}", testUser.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profiles"));
    }
}
