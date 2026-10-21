package com.example.MealShare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.MealShare.entity.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

        boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);
}