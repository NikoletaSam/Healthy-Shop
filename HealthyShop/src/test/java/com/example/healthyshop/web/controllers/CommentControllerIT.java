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
public class CommentControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils testDataUtils;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = testDataUtils.createTestUser("user");
    }

    @AfterEach
    void tearDown() {
        testDataUtils.cleanUpDatabase();
    }

    @Test
    void testAddCommentPageShownByAnonymousUser_Forbidden() throws Exception {
        mockMvc.perform(get("/add/comment"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testAddCommentPageShownByUser() throws Exception {
        mockMvc.perform(get("/add/comment"))
                .andExpect(status().isOk())
                .andExpect(view().name("comment-add"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testAddCommentByUser() throws Exception {
        mockMvc.perform(post("/add/comment")
                .param("text", "testing comment text"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testAddCommentByUserWithInvalidData() throws Exception {
        mockMvc.perform(post("/add/comment")
                .param("text", "ab"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/add/comment"));
    }

    @Test
    void testAddCommentByAnonymous_Forbidden() throws Exception {
        mockMvc.perform(post("/add/comment")
                        .param("text", "testing comment text"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }
}
