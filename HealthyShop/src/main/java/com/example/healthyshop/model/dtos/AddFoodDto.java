package com.example.healthyshop.model.dtos;

import com.example.healthyshop.model.enums.FoodType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class AddFoodDto {

    @NotBlank
    @Size(min = 5)
    private String name;

    @Positive
    private double grams;

    @Positive
    private int calories;

    @NotNull
    @Positive
    private BigDecimal price;

    @NotBlank
    @Size(min = 10)
    private String description;

    @NotBlank
    private String categoryType;

    public AddFoodDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getGrams() {
        return grams;
    }

    public void setGrams(double grams) {
        this.grams = grams;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }
}
