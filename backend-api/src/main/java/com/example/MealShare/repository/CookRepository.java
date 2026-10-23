package com.example.MealShare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.MealShare.entity.Cook;

public interface CookRepository extends JpaRepository<Cook, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndCookIdNot(String email, Long cookId);
}