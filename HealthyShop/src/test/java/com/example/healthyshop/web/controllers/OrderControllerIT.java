package com.example.healthyshop.web.controllers;

import com.example.healthyshop.exceptions.FoodNotFoundException;
import com.example.healthyshop.model.Delivery;
import com.example.healthyshop.model.Food;
import com.example.healthyshop.model.Order;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils testDataUtils;

    private User testUser;
    private Order orderToTest;
    private Delivery deliveryToTest;
    private Food testFood;

    @BeforeEach
    void setUp() {
        testUser = testDataUtils.createTestUser("user");
        orderToTest = testDataUtils.createOrder(testUser);
        testUser.setOrder(orderToTest);
        deliveryToTest = testDataUtils.createDelivery(testUser);
        testUser.setDelivery(deliveryToTest);
        testFood = testDataUtils.createTestFood();
        testDataUtils.addFoodToOrder(orderToTest, testFood);
    }

    @AfterEach
    void tearDown() {
        testDataUtils.cleanUpDatabase();
    }

    @Test
    void testAddFoodToOrderByAnonymous_Forbidden() throws Exception {
        mockMvc.perform(get("/add/food/{id}/to/order", testFood.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testAddFoodToOrderByUser() throws Exception {
        mockMvc.perform(get("/add/food/{id}/to/order", testFood.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/all/food"));
    }

    @Test
    void testReviewOrderByAnonymous_Forbidden() throws Exception {
        mockMvc.perform(get("/order/review"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testReviewOrderByUser() throws Exception {
        mockMvc.perform(get("/order/review"))
                .andExpect(status().isOk())
                .andExpect(view().name("order"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testReviewOrderByUserWithDeliveryDetails() throws Exception {
        mockMvc.perform(get("/order/review")
                        .param("country", "ExampleCountry")
                        .param("city", "ExampleCity")
                        .param("address", "ExampleAddress"))
                .andExpect(status().isOk())
                .andExpect(view().name("order"));
    }

    @Test
    void testRemoveFoodFromOrderByAnonymous_Forbidden() throws Exception {
        mockMvc.perform(get("/remove/food/{id}", testFood.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testRemoveFoodFromOrderByUser() throws Exception {
        testDataUtils.makeFoodUnavailable(testFood);
        mockMvc.perform(get("/remove/food/{id}", testFood.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order/review"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testRemoveUnavailableFoodFromOrderByUser() throws Exception {
        mockMvc.perform(get("/remove/food/{id}", testFood.getId()))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof FoodNotFoundException));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testRemoveNonExistentFoodFromOrderByUser() throws Exception {
        mockMvc.perform(get("/remove/food/{id}", 300))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof FoodNotFoundException));
    }

    @Test
    void testOrderFinishByAnonymous_Forbidden() throws Exception {
        mockMvc.perform(get("/order/finish"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testOrderFinishByUser() throws Exception {
        mockMvc.perform(get("/order/finish"))
                .andExpect(status().isOk())
                .andExpect(view().name("order-ready"));
    }

    //todo make tests for remove/food/id & order/finish
}
