package com.example.healthyshop.web.controllers;

import com.example.healthyshop.model.dtos.DeliveryDetailsDto;
import com.example.healthyshop.model.views.OrderView;
import com.example.healthyshop.service.DeliveryService;
import com.example.healthyshop.service.FoodService;
import com.example.healthyshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller
public class OrderController {
    private OrderService orderService;
    private FoodService foodService;
    private DeliveryService deliveryService;

    @Autowired
    public OrderController(OrderService orderService, FoodService foodService, DeliveryService deliveryService) {
        this.orderService = orderService;
        this.foodService = foodService;
        this.deliveryService = deliveryService;
    }

    @GetMapping("/add/food/{id}/to/order")
    public String addFoodToOrder(@PathVariable("id") Long foodId,
                                 @AuthenticationPrincipal UserDetails userDetails){
        String loggedUserUsername = userDetails.getUsername();

        orderService.addFoodToOrder(loggedUserUsername, foodId);
        foodService.takeFood(foodId);

        return "redirect:/all/food";
    }

    @GetMapping("/order/review")
    public String reviewOrder(Principal principal, Model model){
        String loggedUsername = principal.getName();
        OrderView orderView = orderService.reviewOrder(loggedUsername);

        model.addAttribute("order", orderView);

        if (deliveryService.hasLoggedPersonDeliveryDetails(loggedUsername)){
            model.addAttribute("finishOrder", true);

            DeliveryDetailsDto deliveryDto = deliveryService.findUserDeliveryDetails(loggedUsername);
            model.addAttribute("deliveryDto", deliveryDto);
        } else {
            model.addAttribute("noDetails", true);
        }
        return "order";
    }

    @GetMapping("/remove/food/{id}")
    public String removeFoodFromOrder(@PathVariable("id") Long id,
                                      @AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();

        orderService.removeFoodFromOrder(username, id);
        foodService.makeFoodAvailable(id);

        return "redirect:/order/review";
    }

    @GetMapping("/order/finish")
    public String finishOrder(@AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        orderService.deleteProductsForUser(username);

        return "order-ready";
    }

}
