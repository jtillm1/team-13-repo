package com.example.MealShare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.MealShare.entity.CookEntity;

// gives us built-in database methods for the cooks table
public interface CookRepository extends JpaRepository<CookEntity, Long> {

    // checks whether a cook already uses this email address
    boolean existsByEmail(String email);
}