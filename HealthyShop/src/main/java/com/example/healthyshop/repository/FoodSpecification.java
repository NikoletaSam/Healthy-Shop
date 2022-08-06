package com.example.healthyshop.repository;

import com.example.healthyshop.model.Food;
import com.example.healthyshop.model.dtos.SearchFoodDto;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class FoodSpecification implements Specification<Food> {
    private SearchFoodDto searchFoodDto;

    public FoodSpecification(SearchFoodDto searchFoodDto) {
        this.searchFoodDto = searchFoodDto;
    }

    @Override
    public Predicate toPredicate(Root<Food> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.conjunction();

        if (searchFoodDto.getCategoryType() != null && searchFoodDto.isInstantiated()){
            predicate.getExpressions().add(
                    criteriaBuilder.and(criteriaBuilder.equal(
                            root.join("category").get("type"), searchFoodDto.getCategoryType()
                    )));
        }

        if (searchFoodDto.getMaxPrice() != null){
            predicate.getExpressions().add(
                    criteriaBuilder.lessThanOrEqualTo(root.get("price"), searchFoodDto.getMaxPrice()));
        }

        return predicate;
    }
}
