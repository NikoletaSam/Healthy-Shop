package com.example.healthyshop.model.views;

import java.math.BigDecimal;
import java.util.Set;

public class OrderView {
    private long id;
    String userFullName;
    private Set<FoodOrderView> food;
    private BigDecimal totalValue;

    public OrderView() {
    }

    public OrderView(long id, String userFullName, Set<FoodOrderView> food, BigDecimal totalValue) {
        this.id = id;
        this.userFullName = userFullName;
        this.food = food;
        this.totalValue = totalValue;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public Set<FoodOrderView> getFood() {
        return food;
    }

    public void setFood(Set<FoodOrderView> food) {
        this.food = food;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }
}
