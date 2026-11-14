
package com.example.MealShare.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.MealShare.entity.CookEntity;
import com.example.MealShare.repository.CookRepository;

@Service
public class CookService {

    private final CookRepository cookRepository;

    public CookService(CookRepository cookRepository) {
        this.cookRepository = cookRepository;
    }

    public List<CookEntity> getAllCooks() {
        return cookRepository.findAll();
    }

    public List<CookEntity> getCooksByCuisine(String cuisine) {
        return cookRepository.findAll().stream()
                .filter(c -> c.getCuisine() != null
                        && c.getCuisine().equalsIgnoreCase(cuisine))
                .toList();
    }

    public List<CookEntity> search(
            String name,
            String cuisine,
            String diet) {

        return cookRepository.findAll().stream()
                .filter(c -> name == null || name.isBlank()
                        || (c.getName() != null
                        && c.getName().toLowerCase()
                        .contains(name.toLowerCase().trim())))

                .filter(c -> cuisine == null || cuisine.isBlank()
                        || (c.getCuisine() != null
                        && c.getCuisine().equalsIgnoreCase(cuisine)))

                .filter(c -> diet == null || diet.isBlank()
                        || (c.getDietaryOptions() != null
                        && c.getDietaryOptions().stream()
                        .anyMatch(d -> d.equalsIgnoreCase(diet))))

                .toList();
    }

    public CookEntity getCookById(Long id) {
        return cookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cook not found with id " + id
                ));
    }

    public CookEntity createCook(CookEntity cook) {

        if (cookRepository.existsByEmail(cook.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A cook with that email already exists"
            );
        }

        cook.setId(null);

        if (cook.getJoinedDate() == null) {
            cook.setJoinedDate(LocalDate.now());
        }

        return cookRepository.save(cook);
    }

    public CookEntity updateCook(Long id, CookEntity updated) {

        CookEntity existing = getCookById(id);

        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setEmail(updated.getEmail());
        existing.setCuisine(updated.getCuisine());
        existing.setDietaryOptions(updated.getDietaryOptions());
        existing.setRating(updated.getRating());
        existing.setImageUrl(updated.getImageUrl());

        if (updated.getJoinedDate() != null) {
            existing.setJoinedDate(updated.getJoinedDate());
        }

        if (updated.getPassword() != null
                && !updated.getPassword().isBlank()) {
            existing.setPassword(updated.getPassword());
        }

        return cookRepository.save(existing);
    }

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
