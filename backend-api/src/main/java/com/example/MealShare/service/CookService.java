package com.example.MealShare.service;

import com.example.MealShare.entity.Cook;
import com.example.MealShare.entity.Meal;
import com.example.MealShare.repository.CookRepository;
import com.example.MealShare.repository.MealRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CookService {

    private final CookRepository cookRepository;
    private final MealRepository mealRepository;

    public CookService(CookRepository cookRepository, MealRepository mealRepository) {
        this.cookRepository = cookRepository;
        this.mealRepository = mealRepository;
    }

    // ---------- Cooks ----------

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
        if (cookRepository.existsByEmail(cook.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
        }
        cook.setCookId(0); // force an INSERT; the database generates the id
        return cookRepository.save(cook);
    }

    public Cook updateCook(long id, Cook updated) {
        Cook existing = getCookById(id);
        if (cookRepository.existsByEmailAndCookIdNot(updated.getEmail(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
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

    public void deleteCook(long id) {
        // Loads the cook first so their meals are deleted with them.
        cookRepository.delete(getCookById(id));
    }

    // ---------- Meals (a cook's menu) ----------

    /** A cook's menu, optionally filtered by category and/or dietary tag (both case-insensitive). */
    public List<Meal> getMeals(long cookId, String category, String dietaryTag) {
        getCookById(cookId); // 404 if the cook does not exist

        boolean hasCategory = category != null && !category.isBlank();
        List<Meal> meals = hasCategory
                ? mealRepository.findByCook_CookIdAndCategoryIgnoreCase(cookId, category.trim())
                : mealRepository.findByCook_CookId(cookId);

        if (dietaryTag != null && !dietaryTag.isBlank()) {
            String tag = dietaryTag.trim();
            meals = meals.stream()
                    .filter(m -> m.getDietaryTags().stream().anyMatch(t -> t.equalsIgnoreCase(tag)))
                    .toList();
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