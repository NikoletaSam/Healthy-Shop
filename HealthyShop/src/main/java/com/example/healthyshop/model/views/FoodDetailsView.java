package com.example.healthyshop.model.views;
import java.math.BigDecimal;

public class FoodDetailsView {
    private long id;
    private String name;
    private double grams;
    private int calories;
    private BigDecimal price;
    private String description;
    private String categoryType;

    public FoodDetailsView() {
    }

    public FoodDetailsView(long id, String name, double grams, int calories, BigDecimal price, String description, String categoryType) {
        this.id = id;
        this.name = name;
        this.grams = grams;
        this.calories = calories;
        this.price = price;
        this.description = description;
        this.categoryType = categoryType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
