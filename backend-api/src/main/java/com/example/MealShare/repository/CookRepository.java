package com.example.MealShare.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.MealShare.entity.CookEntity;

@Repository
public interface CookRepository extends JpaRepository<CookEntity, Long> {

    Optional<CookEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    List<CookEntity> findByCuisineIgnoreCase(String cuisine);
}