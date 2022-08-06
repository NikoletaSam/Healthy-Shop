package com.example.healthyshop.web.controllers;

import com.example.healthyshop.model.User;
import com.example.healthyshop.model.dtos.UserRegistrationDto;
import com.example.healthyshop.service.DeliveryService;
import com.example.healthyshop.service.OrderService;
import com.example.healthyshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class UserRegistrationController {
    private UserService userService;
    private OrderService orderService;
    private DeliveryService deliveryService;

    @Autowired
    public UserRegistrationController(UserService userService, OrderService orderService, DeliveryService deliveryService) {
        this.userService = userService;
        this.orderService = orderService;
        this.deliveryService = deliveryService;
    }

    @ModelAttribute("registrationDto")
    public UserRegistrationDto initRegistration(){
        return new UserRegistrationDto();
    }

    @GetMapping("/users/register")
    public String register(){
        return "registration";
    }

    @PostMapping("/users/register")
    public String register(@Valid UserRegistrationDto registrationDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes){

        boolean passMatch = userService.passwordsMatch(registrationDto.getPassword(), registrationDto.getConfirmPassword());
        boolean emailOccupied = userService.isEmailOccupied(registrationDto.getEmail());
        boolean usernameOccupied = userService.isUsernameOccupied(registrationDto.getUsername());

        if (bindingResult.hasErrors() || !passMatch || emailOccupied || usernameOccupied){
            redirectAttributes.addFlashAttribute("registrationDto", registrationDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registrationDto", bindingResult);

            if (emailOccupied){
                redirectAttributes.addFlashAttribute("emailOccupied", true);
            }
            if (usernameOccupied){
                redirectAttributes.addFlashAttribute("usernameOccupied", true);
            }
            return "redirect:/users/register";
        }

        User user = userService.registerAndLogin(registrationDto);
        orderService.createOrderForUser(user);
        deliveryService.createDeliveryForUser(user);

        return "redirect:/";
    }
}
