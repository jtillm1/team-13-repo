package com.example.MealShare.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.MealShare.entity.CookEntity;
import com.example.MealShare.repository.CookRepository;

// service contains the rules and operations used by cook controllers
@Service
public class CookService {

    private final CookRepository cookRepository;

    // spring injects the repository so the service can access the cooks table
    public CookService(CookRepository cookRepository) {
        this.cookRepository = cookRepository;
    }

    // returns every cook stored in the database
    public List<CookEntity> getAllCooks() {
        return cookRepository.findAll();
    }

    // returns only cooks whose cuisine matches the requested cuisine
    public List<CookEntity> getCooksByCuisine(String cuisine) {
        return cookRepository.findAll().stream()
                .filter(c -> c.getCuisine() != null
                        && c.getCuisine().equalsIgnoreCase(cuisine))
                .toList();
    }

    // searches cooks using any combination of name, cuisine, and dietary preference
    public List<CookEntity> search(
            String name,
            String cuisine,
            String diet) {

        return cookRepository.findAll().stream()
                // if no name was entered, keep every cook; otherwise match part of the name
                .filter(c -> name == null || name.isBlank()
                        || (c.getName() != null
                        && c.getName().toLowerCase()
                        .contains(name.toLowerCase().trim())))

                // if no cuisine was selected, keep every cook; otherwise match cuisine
                .filter(c -> cuisine == null || cuisine.isBlank()
                        || (c.getCuisine() != null
                        && c.getCuisine().equalsIgnoreCase(cuisine)))

                // if no diet was selected, keep every cook; otherwise check their diet list
                .filter(c -> diet == null || diet.isBlank()
                        || (c.getDietaryOptions() != null
                        && c.getDietaryOptions().stream()
                        .anyMatch(d -> d.equalsIgnoreCase(diet))))

                // turns the matching stream back into a list for the page
                .toList();
    }

    // finds one cook by id or returns a 404 not found error
    public CookEntity getCookById(Long id) {
        return cookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cook not found with id " + id
                ));
    }

    // checks the email and saves a new cook
    public CookEntity createCook(CookEntity cook) {

        // prevents two cooks from registering with the same email
        if (cookRepository.existsByEmail(cook.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A cook with that email already exists"
            );
        }

        // makes sure the database creates a new id instead of reusing a supplied one
        cook.setId(null);

        // gives the cook today's date if no join date was supplied
        if (cook.getJoinedDate() == null) {
            cook.setJoinedDate(LocalDate.now());
        }

        return cookRepository.save(cook);
    }

    // updates an existing cook while keeping the original database record
    public CookEntity updateCook(Long id, CookEntity updated) {

        // finds the current record first; this also throws 404 if it does not exist
        CookEntity existing = getCookById(id);

        // copies the editable fields from the submitted object to the existing record
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setEmail(updated.getEmail());
        existing.setCuisine(updated.getCuisine());
        existing.setDietaryOptions(updated.getDietaryOptions());
        existing.setRating(updated.getRating());
        existing.setImageUrl(updated.getImageUrl());

        // only changes the join date if a new one was supplied
        if (updated.getJoinedDate() != null) {
            existing.setJoinedDate(updated.getJoinedDate());
        }

        // leaves the old password alone if the request did not include a new one
        if (updated.getPassword() != null
                && !updated.getPassword().isBlank()) {
            existing.setPassword(updated.getPassword());
        }

        return cookRepository.save(existing);
    }

    // deletes a cook after confirming that the id exists
    public void deleteCook(Long id) {

        if (!cookRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Cook not found with id " + id
            );
        }

        cookRepository.deleteById(id);
    }
}