package com.example.healthyshop.service;

import com.example.healthyshop.exceptions.FoodNotFoundException;
import com.example.healthyshop.exceptions.UserNotFoundException;
import com.example.healthyshop.model.*;
import com.example.healthyshop.model.views.FoodOrderView;
import com.example.healthyshop.model.views.FoodView;
import com.example.healthyshop.model.views.OrderView;
import com.example.healthyshop.repository.FoodRepository;
import com.example.healthyshop.repository.OrderRepository;
import com.example.healthyshop.repository.UserRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class OrderService {
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private FoodRepository foodRepository;
    private ModelMapper mapper;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, FoodRepository foodRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.foodRepository = foodRepository;
        this.mapper = new ModelMapper();
    }

    public void createOrderForUser(User user){
        Optional<User> userByUsername = userRepository.findUserByUsername(user.getUsername());

        if (userByUsername.isPresent()){
            Order order = new Order();
            order.setDate(LocalDate.now());
            order.setUser(userByUsername.get());
            orderRepository.save(order);

            userByUsername.get().setOrder(order);
            userRepository.save(userByUsername.get());
        } else {
            throw new UserNotFoundException("User not found.");
        }
    }

    @Transactional
    public void addFoodToOrder(String username, Long foodId){
        Optional<User> userByUsername = userRepository.findUserByUsername(username);
        Optional<Food> food = foodRepository.findById(foodId);

        if (userByUsername.isPresent() && food.isPresent()){
            Order order = userByUsername.get().getOrder();

            order.addFood(food.get());
            food.get().setOrder(order);

            orderRepository.save(order);
        } else if (userByUsername.isEmpty()){
            throw new UserNotFoundException("User not found.");
        } else {
            throw new FoodNotFoundException("Food not found.");
        }
    }

    @Transactional
    public void removeFoodFromOrder(String username, Long foodId){
        Optional<User> userByUsername = userRepository.findUserByUsername(username);
        Optional<Food> food = foodRepository.findById(foodId);

        if (userByUsername.isPresent() && food.isPresent()){
            Order order = userByUsername.get().getOrder();

            order.removeFood(food.get());
            food.get().setOrder(null);

            orderRepository.save(order);
        } else if (userByUsername.isEmpty()){
            throw new UserNotFoundException("User not found.");
        } else {
            throw new FoodNotFoundException("Food not found.");
        }
    }

    @Transactional
    public OrderView reviewOrder(String username){
        Optional<User> userByUsername = userRepository.findUserByUsername(username);

        if (userByUsername.isPresent()){
            Order order = userByUsername.get().getOrder();
            BigDecimal totalPrice = getTotalPrice(order);
            Set<FoodOrderView> foodViews = mapFoodCollection(order.getFood());

            return new OrderView(order.getId(), userByUsername.get().getFullName(), foodViews, totalPrice);
        }

        throw new UserNotFoundException("User not found.");
    }

    private BigDecimal getTotalPrice(Order order){
        BigDecimal totalValue = BigDecimal.ZERO;
        for (Food currentFood : order.getFood()){
            totalValue = totalValue.add(currentFood.getPrice());
        }

        return totalValue;
    }

    private Set<FoodOrderView> mapFoodCollection(Set<Food> foods){
        Set<FoodOrderView> foodViews = new HashSet<>();

        TypeMap<Food, FoodOrderView> typeMap = mapper.getTypeMap(Food.class, FoodOrderView.class);
        if (typeMap == null){
            TypeMap<Food, FoodOrderView> propertyMapper = mapper.createTypeMap(Food.class, FoodOrderView.class);
            Converter<Category, String> categoryToString = mappingContext -> mappingContext.getSource().getType().name();
            propertyMapper.addMappings(m -> m.using(categoryToString)
                    .map(Food::getCategory, FoodOrderView::setCategoryType));
        }

        for (Food currentFood : foods){
            FoodOrderView view = mapper.map(currentFood, FoodOrderView.class);

            foodViews.add(view);
        }

        return foodViews;
    }

    @Transactional
    public void deleteProductsForUser(String username){
        Optional<User> user = userRepository.findUserByUsername(username);

        if (user.isPresent()){
            Order order = user.get().getOrder();
            Set<Food> foodPerOrder = order.getFood();

            order.setFood(new HashSet<>());
            foodRepository.deleteAll(foodPerOrder);
        } else {
            throw new UserNotFoundException("User not found.");
        }
    }
}
