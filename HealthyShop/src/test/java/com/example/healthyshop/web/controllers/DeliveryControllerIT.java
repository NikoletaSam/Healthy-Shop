package com.example.healthyshop.web.controllers;

import com.example.healthyshop.model.Delivery;
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
public class DeliveryControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils testDataUtils;

    private User testUser;
    private Delivery delivery;

    @BeforeEach
    void setUp() {
        testUser = testDataUtils.createTestUser("user");
        delivery = testDataUtils.createDelivery(testUser);
        testUser.setDelivery(delivery);
    }

    @AfterEach
    void tearDown() {
        testDataUtils.cleanUpDatabase();
    }

    @Test
    void testAddDeliveryDetailsPageShownByAnonymous_Forbidden() throws Exception {
        mockMvc.perform(get("/add/delivery/details"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testAddDeliveryDetailsPageShownByUser() throws Exception {
        mockMvc.perform(get("/add/delivery/details"))
                .andExpect(status().isOk())
                .andExpect(view().name("delivery-details"));
    }

    @Test
    void testAddDeliveryDetailsByAnonymous_Forbidden() throws Exception {
        mockMvc.perform(post("/add/delivery/details")
                .param("country", "CountryExample")
                .param("city", "CityExample")
                .param("address", "AddressExample"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testAddDeliveryDetailsByUser() throws Exception {
        mockMvc.perform(post("/add/delivery/details")
                        .param("country", "CountryExample")
                        .param("city", "CityExample")
                        .param("address", "AddressExample"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order/review"));
    }
}
