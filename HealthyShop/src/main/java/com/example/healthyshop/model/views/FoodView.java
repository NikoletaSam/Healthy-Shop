package com.example.healthyshop.model.views;

public class FoodView {
    private long id;
    private String name;
    private String description;
    private String categoryType;

    public FoodView() {
    }

    public FoodView(long id, String name, String description, String categoryType) {
        this.id = id;
        this.name = name;
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
