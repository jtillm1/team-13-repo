package com.example.MealShare.service;

import com.example.MealShare.entity.Cook;
import com.example.MealShare.repository.CookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CookService {

    private final CookRepository cookRepository;

    public CookService(CookRepository cookRepository) {
        this.cookRepository = cookRepository;
    }

    /** Returns all cooks, optionally filtered by partial name and/or exact cuisine. */
    public List<Cook> getCooks(String name, String cuisine) {
        boolean hasName = name != null && !name.isBlank();
        boolean hasCuisine = cuisine != null && !cuisine.isBlank();

        if (hasName && hasCuisine) {
            return cookRepository.findByNameContainingIgnoreCaseAndCuisineIgnoreCase(name.trim(), cuisine.trim());
        }
        if (hasName) {
            return cookRepository.findByNameContainingIgnoreCase(name.trim());
        }
        if (hasCuisine) {
            return cookRepository.findByCuisineIgnoreCase(cuisine.trim());
        }
        return cookRepository.findAll();
    }

    public Cook getCookById(long id) {
        return cookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cook with id " + id + " not found"));
    }

    public Cook createCook(Cook cook) {
        cook.setCookId(0); // force an INSERT; the database generates the id
        return cookRepository.save(cook);
    }

    public Cook updateCook(long id, Cook updated) {
        Cook existing = getCookById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setCuisine(updated.getCuisine());
        existing.setRating(updated.getRating());
        existing.setJoinedDate(updated.getJoinedDate());
        return cookRepository.save(existing);
    }

    public void deleteCook(long id) {
        if (!cookRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cook with id " + id + " not found");
        }
        cookRepository.deleteById(id);
    }
}