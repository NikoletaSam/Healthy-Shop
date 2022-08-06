package com.example.healthyshop.repository;

import com.example.healthyshop.model.Category;
import com.example.healthyshop.model.enums.FoodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findCategoryByType(FoodType foodType);
}
