package com.example.healthyshop.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(targetEntity = Food.class, mappedBy = "order")
    private Set<Food> food;

    @ManyToOne
    private Delivery delivery;

    private LocalDate date;

    @Column(name = "total_value")
    private BigDecimal totalValue;

    @OneToOne(optional = false)
    private User user;

    public Order() {
        this.food = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public Set<Food> getFood() {
        return food;
    }

    public void setFood(Set<Food> food) {
        this.food = food;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addFood(Food food){
        if (this.food == null) {
            this.food = new HashSet<>();
        }
        this.food.add(food);
    }

    public void removeFood(Food food){
        this.food.remove(food);
    }
}
