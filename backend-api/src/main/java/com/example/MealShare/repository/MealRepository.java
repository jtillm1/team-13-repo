package com.example.MealShare.repository;

import com.example.MealShare.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    List<Meal> findByCook_CookId(long cookId);

    List<Meal> findByCook_CookIdAndCategoryIgnoreCase(long cookId, String category);

    Optional<Meal> findByMealIdAndCook_CookId(long mealId, long cookId);
}