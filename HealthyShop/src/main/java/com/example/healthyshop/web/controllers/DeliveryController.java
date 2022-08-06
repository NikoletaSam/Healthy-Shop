package com.example.healthyshop.web.controllers;

import com.example.healthyshop.model.dtos.DeliveryDetailsDto;
import com.example.healthyshop.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DeliveryController {
    private DeliveryService deliveryService;

    @Autowired
    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @ModelAttribute("deliveryDto")
    public DeliveryDetailsDto init(){
        return new DeliveryDetailsDto();
    }

    @GetMapping("/add/delivery/details")
    public String addDelivery(){
        return "delivery-details";
    }

    @PostMapping("/add/delivery/details")
    public String addDelivery(DeliveryDetailsDto deliveryDto,
                              @AuthenticationPrincipal UserDetails userDetails){
        String loggedUsername = userDetails.getUsername();
        deliveryService.addDeliveryDetails(deliveryDto, loggedUsername);

        return "redirect:/order/review";
    }
}
