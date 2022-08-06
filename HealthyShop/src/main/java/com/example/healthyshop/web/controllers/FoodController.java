package com.example.healthyshop.web.controllers;

import com.example.healthyshop.model.dtos.AddFoodDto;
import com.example.healthyshop.model.dtos.SearchFoodDto;
import com.example.healthyshop.model.views.FoodDetailsView;
import com.example.healthyshop.model.views.FoodView;
import com.example.healthyshop.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
public class FoodController {
    private FoodService foodService;

    @Autowired
    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @ModelAttribute("foodDto")
    public AddFoodDto init(){
        return new AddFoodDto();
    }

    @GetMapping("/food/add")
    public String addFood(){
        return "food-add";
    }

    @PostMapping("/food/add")
    public String addFood(@Valid AddFoodDto foodDto,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("foodDto", foodDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.foodDto", bindingResult);

            return "redirect:/food/add";
        }

        foodService.addFood(foodDto);
        return "redirect:/";
    }

    @GetMapping("/all/food")
    public String allFood(Model model){
        List<FoodView> allFood = foodService.getAllAvailableFood();
        model.addAttribute("food", allFood);

        return "food";
    }

    @GetMapping("/food/details/{id}")
    public String foodDetails(@PathVariable("id") long foodId,
                              Model model){
        FoodDetailsView foodDetails = foodService.getFoodDetails(foodId);
        model.addAttribute("foodDetails", foodDetails);

        return "food-details";
    }

    @GetMapping("/food/search")
    public String searchFood(SearchFoodDto searchFoodDto, Model model){
        if (!model.containsAttribute("searchFoodDto")){
            model.addAttribute("searchFoodDto", searchFoodDto);
        }

        if (searchFoodDto.isInstantiated()){
            model.addAttribute("foodSearch", foodService.searchFood(searchFoodDto));
        }

        return "food-search";
    }
}
