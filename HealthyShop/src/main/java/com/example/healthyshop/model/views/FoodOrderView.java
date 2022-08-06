package com.example.healthyshop.model.views;

import java.math.BigDecimal;

public class FoodOrderView {
    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String categoryType;

    public FoodOrderView() {
    }

    public FoodOrderView(long id, String name, String description, String categoryType, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categoryType = categoryType;
        this.price = price;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
