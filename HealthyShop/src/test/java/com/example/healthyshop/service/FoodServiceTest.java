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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FoodServiceTest {
    private FoodService toTest;

    @Mock
    private FoodRepository mockFoodRepository;

    @Mock
    private CategoryRepository mockCategoryRepository;

    @BeforeEach
    void setUp() {
        toTest = new FoodService(mockFoodRepository, mockCategoryRepository);
    }

    @Test
    void testAddFood() {
        AddFoodDto addFoodDto = new AddFoodDto();
        addFoodDto.setCalories(350);
        addFoodDto.setCategoryType("SALAD");
        addFoodDto.setDescription("Test food salad description.");
        addFoodDto.setGrams(600);
        addFoodDto.setName("Test Salad");
        addFoodDto.setPrice(BigDecimal.valueOf(7.80));

        Food testFood = new Food();
        when(mockFoodRepository.save(Mockito.any(Food.class))).thenReturn(testFood);
        when(mockCategoryRepository.findCategoryByType(FoodType.SALAD)).thenReturn(Optional.of(new Category(FoodType.SALAD)));

        testFood = toTest.addFood(addFoodDto);

        Assertions.assertEquals(600, testFood.getGrams());
    }

    @Test
    void testGetAllAvailableFood() {
        Food firstFood = new Food("firstFood", 500,
                "test first food Description",
                new Category(FoodType.SALAD),
                BigDecimal.valueOf(10),
                400, true);

        Food secondFood = new Food("secondFood", 500,
                "test second food Description",
                new Category(FoodType.SALAD),
                BigDecimal.valueOf(10),
                400, false);

        Food thirdFood = new Food("thirdFood", 500,
                "test third food Description",
                new Category(FoodType.SALAD),
                BigDecimal.valueOf(10),
                400, true);

        when(mockFoodRepository.findAll()).thenReturn(List.of(firstFood, secondFood, thirdFood));

        List<FoodView> availableFood = toTest.getAllAvailableFood();

        Assertions.assertEquals(2, availableFood.size());
    }

    @Test
    void testGetFoodDetails() {
        Food firstFood = new Food("firstFood", 500,
                "test first food Description",
                new Category(FoodType.SALAD),
                BigDecimal.valueOf(10),
                400, true);

        when(mockFoodRepository.findById(1L)).thenReturn(Optional.of(firstFood));

        FoodDetailsView foodDetails = toTest.getFoodDetails(1L);

        Assertions.assertEquals("test first food Description", foodDetails.getDescription());
        Assertions.assertEquals(500, foodDetails.getGrams());
        Assertions.assertEquals(400, foodDetails.getCalories());
    }

    @Test
    void testGetFoodDetailsThrowsExceptionForNonExistentId() {
        Assertions.assertThrows(FoodNotFoundException.class, () -> toTest.getFoodDetails(3L));
    }

    @Test
    void testTakeFoodForValidFoodId() {
        Food firstFood = new Food("firstFood", 500,
                "test first food Description",
                new Category(FoodType.SALAD),
                BigDecimal.valueOf(10),
                400, true);

        when(mockFoodRepository.findById(1L)).thenReturn(Optional.of(firstFood));

        toTest.takeFood(1L);

        Assertions.assertFalse(firstFood.isAvailable());
    }

    @Test
    void testTakeFoodThrowsExceptionForNonAvailableFood() {
        Food firstFood = new Food("firstFood", 500,
                "test first food Description",
                new Category(FoodType.SALAD),
                BigDecimal.valueOf(10),
                400, false);

        when(mockFoodRepository.findById(1L)).thenReturn(Optional.of(firstFood));

        Assertions.assertThrows(FoodNotFoundException.class, () -> toTest.takeFood(1L));
    }

    @Test
    void testTakeFoodThrowsExceptionForNonExistentFood() {
        Assertions.assertThrows(FoodNotFoundException.class, () -> toTest.takeFood(15L));
    }

    @Test
    void testMakeFoodAvailableForValidFoodId() {
        Food firstFood = new Food("firstFood", 500,
                "test first food Description",
                new Category(FoodType.SALAD),
                BigDecimal.valueOf(10),
                400, false);

        when(mockFoodRepository.findById(1L)).thenReturn(Optional.of(firstFood));

        toTest.makeFoodAvailable(1L);

        Assertions.assertTrue(firstFood.isAvailable());
    }

    @Test
    void testMakeFoodAvailableThrowsExceptionForAvailableFood() {
        Food firstFood = new Food("firstFood", 500,
                "test first food Description",
                new Category(FoodType.SALAD),
                BigDecimal.valueOf(10),
                400, true);

        when(mockFoodRepository.findById(1L)).thenReturn(Optional.of(firstFood));

        Assertions.assertThrows(FoodNotFoundException.class, () -> toTest.makeFoodAvailable(1L));
    }

    @Test
    void testMakeFoodAvailableThrowsExceptionForNonExistentFood() {
        Assertions.assertThrows(FoodNotFoundException.class, () -> toTest.makeFoodAvailable(15L));
    }
}
