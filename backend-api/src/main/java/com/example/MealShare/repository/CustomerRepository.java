package com.example.MealShare.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.MealShare.entity.CustomerEntity;

// gives the application standard database operations for CustomerEntity
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    // checks whether an email is already registered
    boolean existsByEmail(String email);

    // looks up one customer by email and returns an optional in case none is found
    Optional<CustomerEntity> findByEmail(String email);
}