package com.example.healthyshop.service;

import com.example.healthyshop.exceptions.FoodNotFoundException;
import com.example.healthyshop.model.Category;
import com.example.healthyshop.model.Food;
import com.example.healthyshop.model.dtos.AddFoodDto;
import com.example.healthyshop.model.dtos.SearchFoodDto;
import com.example.healthyshop.model.enums.FoodType;
import com.example.healthyshop.model.views.FoodDetailsView;
import com.example.healthyshop.model.views.FoodView;
import com.example.healthyshop.repository.CategoryRepository;
import com.example.healthyshop.repository.FoodRepository;
import com.example.healthyshop.repository.FoodSpecification;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FoodService {
    private FoodRepository foodRepository;
    private CategoryRepository categoryRepository;
    private ModelMapper mapper;

    @Autowired
    public FoodService(FoodRepository foodRepository, CategoryRepository categoryRepository) {
        this.foodRepository = foodRepository;
        this.categoryRepository = categoryRepository;
        this.mapper = new ModelMapper();
    }

    public Food addFood(AddFoodDto foodDto){
        TypeMap<AddFoodDto, Food> propertyMapper = mapper.createTypeMap(AddFoodDto.class, Food.class);
        Converter<String, Category> stringToCategory = mappingContext -> categoryRepository.findCategoryByType(FoodType.valueOf(mappingContext.getSource())).get();
        propertyMapper
                .addMappings(m -> m.using(stringToCategory).map(AddFoodDto::getCategoryType, Food::setCategory));

        Food food = mapper.map(foodDto, Food.class);
        food.setAvailable(true);

       foodRepository.save(food);
       return food;
    }

    public List<FoodView> getAllAvailableFood(){
        List<Food> foods = this.foodRepository.findAll()
                .stream()
                .filter(Food::isAvailable)
                .toList();;
        List<FoodView> views = new ArrayList<>();

        this.initTypeMapFoodToFoodView();

        for (Food currentFood : foods){
            FoodView currentView = mapper.map(currentFood, FoodView.class);
            views.add(currentView);
        }
        return views;
    }

    public FoodDetailsView getFoodDetails(Long foodId){
        Optional<Food> food = foodRepository.findById(foodId);

        TypeMap<Food, FoodDetailsView> typeMap = mapper.getTypeMap(Food.class, FoodDetailsView.class);

        if (typeMap == null){
            TypeMap<Food, FoodDetailsView> propertyMapper = mapper.createTypeMap(Food.class, FoodDetailsView.class);
            Converter<Category, String> categoryToString = mappingContext -> mappingContext.getSource().getType().name();
            propertyMapper.addMappings(m -> m.using(categoryToString)
                    .map(Food::getCategory, FoodDetailsView::setCategoryType));
        }

        return food.map(value -> mapper.map(value, FoodDetailsView.class)).orElseThrow(() -> new FoodNotFoundException("Food was not found."));

    }

    public void takeFood(long foodId){
        Optional<Food> food = foodRepository.findById(foodId);
        if (food.isPresent() && food.get().isAvailable()){
            food.get().setAvailable(false);
            foodRepository.save(food.get());
        } else {
            throw new FoodNotFoundException("Food was not found.");
        }
    }

    public void makeFoodAvailable(long foodId){
        Optional<Food> food = foodRepository.findById(foodId);
        if (food.isPresent() && !food.get().isAvailable()){
            food.get().setAvailable(true);
            foodRepository.save(food.get());
        } else {
            throw new FoodNotFoundException("Food was not found.");
        }
    }

    public Set<FoodView> searchFood(SearchFoodDto searchFoodDto){
        Set<Food> foods = this.foodRepository
                .findAll(new FoodSpecification(searchFoodDto))
                .stream()
                .filter(Food::isAvailable)
                .collect(Collectors.toSet());

        Set<FoodView> views = new HashSet<>();

        this.initTypeMapFoodToFoodView();

        for (Food currentFood : foods){
            FoodView view = mapper.map(currentFood, FoodView.class);
            views.add(view);
        }

        return views;
    }

    private void initTypeMapFoodToFoodView(){
        TypeMap<Food, FoodView> typeMap = mapper.getTypeMap(Food.class, FoodView.class);
        if (typeMap == null){
            TypeMap<Food, FoodView> propertyMapper = mapper.createTypeMap(Food.class, FoodView.class);
            Converter<Category, String> categoryToString = mappingContext -> mappingContext.getSource().getType().name();
            propertyMapper.addMappings(m -> m.using(categoryToString)
                    .map(Food::getCategory, FoodView::setCategoryType));
        }
    }
}
