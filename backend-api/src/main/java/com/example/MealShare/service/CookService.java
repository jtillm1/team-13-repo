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
        return cookRepository.findByCuisineIgnoreCase(cuisine);
    }

    public CookEntity getCookById(Long id) {
        return cookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cook not found with id " + id));
    }

    public CookEntity createCook(CookEntity cook) {
        if (cookRepository.existsByEmail(cook.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "A cook with that email already exists");
        }
        cook.setId(null); // make sure this is always an insert
        if (cook.getJoinedDate() == null) {
            cook.setJoinedDate(LocalDate.now());
        }
        // TODO: hash cook.getPassword() (e.g. BCrypt via Spring Security) before saving
        return cookRepository.save(cook);
    }

    public CookEntity updateCook(Long id, CookEntity updated) {
        CookEntity existing = getCookById(id);

        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setEmail(updated.getEmail());
        existing.setCuisine(updated.getCuisine());
        existing.setRating(updated.getRating());
        existing.setJoinedDate(updated.getJoinedDate());

        // only change the password if a new one was sent
        if (updated.getPassword() != null && !updated.getPassword().isBlank()) {
            existing.setPassword(updated.getPassword()); // TODO: hash this too
        }
        return cookRepository.save(existing);
    }

    public void deleteCook(Long id) {
        if (!cookRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Cook not found with id " + id);
        }
        cookRepository.deleteById(id);
    }
}