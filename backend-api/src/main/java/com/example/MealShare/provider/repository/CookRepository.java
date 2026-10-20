package com.example.MealShare.provider.repository;

import com.example.MealShare.provider.entity.Cook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CookRepository extends JpaRepository<Cook, Long> {

    List<Cook> findByCuisineIgnoreCase(String cuisine);

    List<Cook> findByNameContainingIgnoreCase(String name);

    List<Cook> findByNameContainingIgnoreCaseAndCuisineIgnoreCase(String name, String cuisine);
}