package com.example.healthyshop.service;

import com.example.healthyshop.model.Category;
import com.example.healthyshop.model.enums.FoodType;
import com.example.healthyshop.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CategoryService {
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void initCategories(){
        if (categoryRepository.count() == 0){
            List<Category> categories = Arrays.stream(FoodType.values())
                    .map(Category::new)
                    .toList();

            categoryRepository.saveAll(categories);
        }
    }
}
