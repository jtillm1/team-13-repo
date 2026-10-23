package com.example.MealShare.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.MealShare.entity.Cook;
import com.example.MealShare.repository.CookRepository;

@Service
public class CookService {

    private final CookRepository cookRepository;

    public CookService(CookRepository cookRepository) {
        this.cookRepository = cookRepository;
    }

    // Register a new cook
    public Cook create(Cook cook) {
        if (cookRepository.existsByEmail(cook.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email is already registered");
        }

        cook.setCookId(null);
        return cookRepository.save(cook);
    }

    // Get all cooks
    public List<Cook> getAll() {
        return cookRepository.findAll();
    }

    // Get one cook by ID
    public Cook getById(Long id) {
        return cookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cook not found"));
    }

    // Update an existing cook
    public Cook update(Long id, Cook updated) {
        Cook existing = getById(id);

        if (cookRepository.existsByEmailAndCookIdNot(
                updated.getEmail(), id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email is already registered");
        }

        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setEmail(updated.getEmail());
        existing.setPassword(updated.getPassword());
        existing.setCuisine(updated.getCuisine());
        existing.setRating(updated.getRating());
        existing.setJoinedDate(updated.getJoinedDate());

        return cookRepository.save(existing);
    }

    // Delete an existing cook
    public void delete(Long id) {
        Cook existing = getById(id);
        cookRepository.delete(existing);
    }
}