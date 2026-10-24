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
    private final MealRepository mealRepository;

    public CookService(CookRepository cookRepository, MealRepository mealRepository) {
        this.cookRepository = cookRepository;
        this.mealRepository = mealRepository;
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
        return meals;
    }

    public Meal getMeal(long cookId, long mealId) {
        getCookById(cookId);
        return mealRepository.findByMealIdAndCook_CookId(mealId, cookId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Meal " + mealId + " not found for cook " + cookId));
    }

    public Meal createMeal(long cookId, Meal meal) {
        Cook cook = getCookById(cookId);
        meal.setMealId(0); // force an INSERT
        meal.setCook(cook);
        return mealRepository.save(meal);
    }

    public Meal updateMeal(long cookId, long mealId, Meal updated) {
        Meal existing = getMeal(cookId, mealId);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setCategory(updated.getCategory());
        existing.setPrice(updated.getPrice());
        existing.setDietaryTags(updated.getDietaryTags());
        return mealRepository.save(existing);
    }

    public void deleteMeal(long cookId, long mealId) {
        mealRepository.delete(getMeal(cookId, mealId));
    }
}