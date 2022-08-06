package com.example.healthyshop.model.dtos;

import com.example.healthyshop.model.enums.FoodType;

import java.math.BigDecimal;

public class SearchFoodDto {
    private FoodType categoryType;
    private BigDecimal maxPrice;

    public SearchFoodDto() {
    }

    public FoodType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(FoodType categoryType) {
        this.categoryType = categoryType;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public boolean isInstantiated(){
        return (this.categoryType != null) || this.maxPrice != null;
    }
}
