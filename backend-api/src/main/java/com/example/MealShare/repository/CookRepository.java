
package com.example.MealShare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.MealShare.entity.CookEntity;

public interface CookRepository extends JpaRepository<CookEntity, Long> {

    boolean existsByEmail(String email);

}
