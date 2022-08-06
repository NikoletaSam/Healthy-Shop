package com.example.healthyshop.util;

import com.example.healthyshop.model.*;
import com.example.healthyshop.model.enums.FoodType;
import com.example.healthyshop.model.enums.RoleName;
import com.example.healthyshop.repository.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;

@Component
public class TestDataUtils {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private FoodRepository foodRepository;
    private CategoryRepository categoryRepository;
    private CommentRepository commentRepository;
    private DeliveryRepository deliveryRepository;
    private OrderRepository orderRepository;

    public TestDataUtils(UserRepository userRepository, RoleRepository roleRepository, FoodRepository foodRepository, CategoryRepository categoryRepository, CommentRepository commentRepository, DeliveryRepository deliveryRepository, OrderRepository orderRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.foodRepository = foodRepository;
        this.categoryRepository = categoryRepository;
        this.commentRepository = commentRepository;
        this.deliveryRepository = deliveryRepository;
        this.orderRepository = orderRepository;
    }

    private void initRoles() {
        if (roleRepository.count() == 0){
            Role admin = new Role(RoleName.ADMIN);
            roleRepository.save(admin);

            Role user = new Role(RoleName.USER);
            roleRepository.save(user);
        }
    }

    private void initCategories() {
        if (categoryRepository.count() == 0){
            Category appetizer = new Category(FoodType.APPETIZER);
            categoryRepository.save(appetizer);

            Category salad = new Category(FoodType.SALAD);
            categoryRepository.save(salad);

            Category dessert = new Category(FoodType.DESSERT);
            categoryRepository.save(dessert);
        }
    }

    public User createTestAdmin(String username) {
        initRoles();

        User admin = new User(username, "1234567", "admin@test.com", "Admin Adminov", 20);
        admin.setRoles(new HashSet<>(roleRepository.findAll()));

        return userRepository.save(admin);
    }

    public User createTestUser(String username) {
        initRoles();

        User user = new User(username, "1234567", "user@test.com", "User Userov", 21);
        user.addRole(roleRepository.findByName(RoleName.USER).get());

        return userRepository.save(user);
    }

    public Delivery createDelivery(User user) {
        Delivery delivery = new Delivery();
        delivery.setUser(user);

        return deliveryRepository.save(delivery);
    }

    public Order createOrder(User user){
        Order order = new Order();
        order.setUser(user);

        return orderRepository.save(order);
    }

    public Food createTestFood() {
        initCategories();

        Food food = new Food("food", 350, "food test example description",
                categoryRepository.findCategoryByType(FoodType.SALAD).get(),
                BigDecimal.valueOf(7.80), 200, true);

        return foodRepository.save(food);
    }

    public void makeFoodUnavailable(Food food){
        food.setAvailable(false);
        foodRepository.save(food);
    }

    public void addFoodToOrder(Order order, Food food){
        order.addFood(food);
        orderRepository.save(order);
    }

    public void cleanUpDatabase() {
        foodRepository.deleteAll();
        commentRepository.deleteAll();
        deliveryRepository.deleteAll();
        orderRepository.deleteAll();
        userRepository.deleteAll();
        categoryRepository.deleteAll();
        roleRepository.deleteAll();
    }
}
