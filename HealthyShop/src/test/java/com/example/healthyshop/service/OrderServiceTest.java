package com.example.healthyshop.service;

import com.example.healthyshop.exceptions.FoodNotFoundException;
import com.example.healthyshop.exceptions.UserNotFoundException;
import com.example.healthyshop.model.*;
import com.example.healthyshop.model.enums.FoodType;
import com.example.healthyshop.model.enums.RoleName;
import com.example.healthyshop.model.views.OrderView;
import com.example.healthyshop.repository.FoodRepository;
import com.example.healthyshop.repository.OrderRepository;
import com.example.healthyshop.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    private OrderService toTest;

    @Mock
    private OrderRepository mockOrderRepository;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private FoodRepository mockFoodRepository;

    @BeforeEach
    void setUp() {
        toTest = new OrderService(mockOrderRepository, mockUserRepository, mockFoodRepository);
    }

    @Test
    void testCreateOrderForValidUser() {
        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("1234567");
        testUser.setAge(20);
        testUser.setFullName("User Testov");
        testUser.setEmail("user@testov.com");
        testUser.setRoles(Set.of(
                new Role(RoleName.USER),
                new Role(RoleName.ADMIN)));

        when(mockUserRepository.findUserByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        toTest.createOrderForUser(testUser);

        Assertions.assertNotNull(testUser.getOrder());
    }

    @Test
    void testCreateOrderThrowsExceptionForNonExistentUser() {
        Assertions.assertThrows(UserNotFoundException.class, () -> toTest.createOrderForUser(new User()));
    }

    @Test
    void testAddFoodToOrder(){
        Order testOrder = new Order();

        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("1234567");
        testUser.setAge(20);
        testUser.setFullName("User Testov");
        testUser.setEmail("user@testov.com");
        testUser.setRoles(Set.of(
                new Role(RoleName.USER),
                new Role(RoleName.ADMIN)));
        testUser.setOrder(testOrder);

        Food testFood = new Food("firstFood", 500,
                "test first food Description",
                new Category(FoodType.SALAD),
                BigDecimal.valueOf(10),
                400, true);

        when(mockUserRepository.findUserByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        when(mockFoodRepository.findById(1L)).thenReturn(Optional.of(testFood));

        toTest.addFoodToOrder(testUser.getUsername(), 1L);

        Assertions.assertEquals(1, testOrder.getFood().size());
    }

    @Test
    void testAddFoodToOrderThrowsExceptionForNonExistentUser() {
        Order testOrder = new Order();

        Food testFood = new Food("firstFood", 500,
                "test first food Description",
                new Category(FoodType.SALAD),
                BigDecimal.valueOf(10),
                400, true);

        when(mockFoodRepository.findById(1L)).thenReturn(Optional.of(testFood));

        Assertions.assertThrows(UserNotFoundException.class, () -> toTest.addFoodToOrder("unknownUser", 1L));
    }

    @Test
    void testAddFoodToOrderThrowsExceptionForNonExistentFood() {
        Order testOrder = new Order();

        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("1234567");
        testUser.setAge(20);
        testUser.setFullName("User Testov");
        testUser.setEmail("user@testov.com");
        testUser.setRoles(Set.of(
                new Role(RoleName.USER),
                new Role(RoleName.ADMIN)));
        testUser.setOrder(testOrder);

        when(mockUserRepository.findUserByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        Assertions.assertThrows(FoodNotFoundException.class, () -> toTest.addFoodToOrder(testUser.getUsername(), 1L));
    }

    @Test
    void testRemoveFoodFromOrder() {
        Order testOrder = new Order();

        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("1234567");
        testUser.setAge(20);
        testUser.setFullName("User Testov");
        testUser.setEmail("user@testov.com");
        testUser.setRoles(Set.of(
                new Role(RoleName.USER),
                new Role(RoleName.ADMIN)));
        testUser.setOrder(testOrder);

        Food testFood = new Food("firstFood", 500,
                "test first food Description",
                new Category(FoodType.SALAD),
                BigDecimal.valueOf(10),
                400, false);

        testOrder.addFood(testFood);

        when(mockUserRepository.findUserByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        when(mockFoodRepository.findById(1L)).thenReturn(Optional.of(testFood));

        toTest.removeFoodFromOrder(testUser.getUsername(), 1L);

        Assertions.assertEquals(0, testUser.getOrder().getFood().size());
    }

    @Test
    void testRemoveFoodFromOrderThrowsExceptionForNonExistentUser() {
        Order testOrder = new Order();

        Food testFood = new Food("firstFood", 500,
                "test first food Description",
                new Category(FoodType.SALAD),
                BigDecimal.valueOf(10),
                400, false);

        testOrder.addFood(testFood);

        when(mockFoodRepository.findById(1L)).thenReturn(Optional.of(testFood));

        Assertions.assertThrows(UserNotFoundException.class, () -> toTest.removeFoodFromOrder("unknownUser", 1L));
    }

    @Test
    void testRemoveFoodFromOrderThrowsExceptionForNonExistentFood() {
        Order testOrder = new Order();

        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("1234567");
        testUser.setAge(20);
        testUser.setFullName("User Testov");
        testUser.setEmail("user@testov.com");
        testUser.setRoles(Set.of(
                new Role(RoleName.USER),
                new Role(RoleName.ADMIN)));
        testUser.setOrder(testOrder);
        when(mockUserRepository.findUserByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        Assertions.assertThrows(FoodNotFoundException.class, () -> toTest.removeFoodFromOrder(testUser.getUsername(), 10L));
    }

    @Test
    void testReviewOrderForValidData() {
        Order testOrder = new Order();

        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("1234567");
        testUser.setAge(20);
        testUser.setFullName("User Testov");
        testUser.setEmail("user@testov.com");
        testUser.setRoles(Set.of(
                new Role(RoleName.USER),
                new Role(RoleName.ADMIN)));
        testUser.setOrder(testOrder);

        Food testFood = new Food("firstFood", 500,
                "test first food Description",
                new Category(FoodType.SALAD),
                BigDecimal.valueOf(10),
                400, false);
        Food secondFood = new Food("secondFood", 500,
                "test second food Description",
                new Category(FoodType.SALAD),
                BigDecimal.valueOf(10),
                400, false);

        testOrder.addFood(testFood);
        testOrder.addFood(secondFood);

        when(mockUserRepository.findUserByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        OrderView orderView = toTest.reviewOrder(testUser.getUsername());

        Assertions.assertEquals(2, orderView.getFood().size());
        Assertions.assertEquals(BigDecimal.valueOf(20), orderView.getTotalValue());
    }

    @Test
    void testReviewOrderForNonExistentUser() {
        Order testOrder = new Order();
        Food testFood = new Food("firstFood", 500,
                "test first food Description",
                new Category(FoodType.SALAD),
                BigDecimal.valueOf(10),
                400, false);
        Food secondFood = new Food("secondFood", 500,
                "test second food Description",
                new Category(FoodType.SALAD),
                BigDecimal.valueOf(10),
                400, false);

        testOrder.addFood(testFood);
        testOrder.addFood(secondFood);

        Assertions.assertThrows(UserNotFoundException.class, () -> toTest.reviewOrder("unknownUser"));
    }

    @Test
    void testDeleteProductsForUser() {
        Order testOrder = new Order();

        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("1234567");
        testUser.setAge(20);
        testUser.setFullName("User Testov");
        testUser.setEmail("user@testov.com");
        testUser.setRoles(Set.of(
                new Role(RoleName.USER),
                new Role(RoleName.ADMIN)));
        testUser.setOrder(testOrder);

        Food testFood = new Food("firstFood", 500,
                "test first food Description",
                new Category(FoodType.SALAD),
                BigDecimal.valueOf(10),
                400, false);
        Food secondFood = new Food("secondFood", 500,
                "test second food Description",
                new Category(FoodType.SALAD),
                BigDecimal.valueOf(10),
                400, false);

        testOrder.addFood(testFood);
        testOrder.addFood(secondFood);

        when(mockUserRepository.findUserByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        toTest.deleteProductsForUser(testUser.getUsername());

        Assertions.assertEquals(0, testOrder.getFood().size());
    }

    @Test
    void testDeleteProductsThrowsExceptionForNonExistentUser() {
        Order testOrder = new Order();
        Food testFood = new Food("firstFood", 500,
                "test first food Description",
                new Category(FoodType.SALAD),
                BigDecimal.valueOf(10),
                400, false);
        Food secondFood = new Food("secondFood", 500,
                "test second food Description",
                new Category(FoodType.SALAD),
                BigDecimal.valueOf(10),
                400, false);

        testOrder.addFood(testFood);
        testOrder.addFood(secondFood);

        Assertions.assertThrows(UserNotFoundException.class, () -> toTest.deleteProductsForUser("unknownUser"));
    }
}
