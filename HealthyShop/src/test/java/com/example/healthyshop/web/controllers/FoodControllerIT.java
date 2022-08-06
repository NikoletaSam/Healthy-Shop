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
public class FoodControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils testDataUtils;

    private User testAdmin;
    private User testUser;
    private Food testFoodSalad;

    @BeforeEach
    void setUp() {
        testAdmin = testDataUtils.createTestAdmin("admin");
        testUser = testDataUtils.createTestUser("user");
        testFoodSalad = testDataUtils.createTestFood();
    }

    @AfterEach
    void tearDown() {
        testDataUtils.cleanUpDatabase();
    }

    @Test
    void testAddFoodPageShownByAnonymousUser_Forbidden() throws Exception {
        mockMvc.perform(get("/food/add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void testAddFoodPageShownByAdmin() throws Exception {
        mockMvc.perform(get("/food/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("food-add"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testAddFoodPageShownByUser_Forbidden() throws Exception {
        mockMvc.perform(get("/food/add"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAddFoodByAnonymousUser_Forbidden() throws Exception {
        mockMvc.perform(post("/food/add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void testAddFoodByAdmin() throws Exception {
        mockMvc.perform(post("/food/add")
                        .param("name", "salad")
                        .param("grams", "300")
                        .param("calories", "800")
                        .param("price", "4.90")
                        .param("description", "the best first food example")
                        .param("categoryType", "SALAD"))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void testAdFoodByAdminWithInvalidData() throws Exception {
        mockMvc.perform(post("/food/add")
                        .param("name", "salad")
                        .param("grams", "300")
                        .param("calories", "800")
                        .param("price", "4.90")
                        .param("description", "desc")
                        .param("categoryType", "SALAD"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/food/add"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testAddFoodByUser_Forbidden() throws Exception {
        mockMvc.perform(post("/food/add")
                        .param("name", "salad")
                        .param("grams", "300")
                        .param("calories", "800")
                        .param("price", "4.90")
                        .param("description", "the best first food example")
                        .param("categoryType", "SALAD"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testViewAllFood() throws Exception {
        mockMvc.perform(get("/all/food"))
                .andExpect(status().isOk())
                .andExpect(view().name("food"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testViewFoodDetailsByLoggedUser() throws Exception {
        mockMvc.perform(get("/food/details/{id}", testFoodSalad.getId()))
                .andExpect(view().name("food-details"));
    }

    @Test
    void testViewFoodDetailsByAnonymous_Forbidden() throws Exception {
        mockMvc.perform(get("/food/details/{id}", testFoodSalad.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    void testSearchFood() throws Exception {
        mockMvc.perform(get("/food/search"))
                .andExpect(status().isOk())
                .andExpect(view().name("food-search"));
    }

    @Test
    void testSearchFoodWithParameters() throws Exception {
        mockMvc.perform(get("/food/search")
                        .param("categoryType", "SALAD")
                        .param("maxPrice", "1000"))
                .andExpect(status().isOk())
                .andExpect(view().name("food-search"));
    }
}
