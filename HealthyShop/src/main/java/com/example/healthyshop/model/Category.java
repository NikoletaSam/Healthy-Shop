package com.example.healthyshop.model;

import com.example.healthyshop.model.enums.FoodType;

import javax.persistence.*;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private FoodType type;

    @Column(columnDefinition = "TEXT")
    private String description;

    public Category() {
    }

    public Category(FoodType type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public FoodType getType() {
        return type;
    }

    public void setType(FoodType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
