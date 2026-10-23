package com.example.MealShare.repository;

import com.example.MealShare.entity.Cook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CookRepository extends JpaRepository<Cook, Long> {

    List<Cook> findByCuisineIgnoreCase(String cuisine);

    List<Cook> findByNameContainingIgnoreCase(String name);

    List<Cook> findByNameContainingIgnoreCaseAndCuisineIgnoreCase(String name, String cuisine);

    boolean existsByEmail(String email);

    boolean existsByEmailAndCookIdNot(String email, long cookId);
}