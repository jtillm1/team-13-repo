
package com.example.MealShare.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.MealShare.entity.CookEntity;
import com.example.MealShare.repository.CookRepository;

@Component
public class DemoDataSeeder implements CommandLineRunner {

    private final CookRepository cookRepository;

    public DemoDataSeeder(CookRepository cookRepository) {
        this.cookRepository = cookRepository;
    }

    @Override
    public void run(String... args) {

        List<CookEntity> cooks = cookRepository.findAll();

        // Stop if the three profiles have already been updated.
        boolean alreadyUpdated = cooks.stream().anyMatch(
                c -> "Sofia's Cocina".equals(c.getName()));

        if (alreadyUpdated) {
            return;
        }

        // Only update the three existing demo cooks.
        if (cooks.size() != 3) {
            System.out.println(
                    "Demo seeder skipped: expected exactly 3 cooks.");
            return;
        }

        // Keep existing database IDs and order records by ID.
        cooks.sort(Comparator.comparing(CookEntity::getId));

        updateCook(
                cooks.get(0),
                "Sofia's Cocina",
                "Mexican",
                List.of("Vegetarian"),
                "Bright, veggie-forward Mexican dishes made from scratch.",
                4.9,
                "/images/sofia.jpg"
        );

        updateCook(
                cooks.get(1),
                "Hana's Kitchen",
                "Asian",
                List.of("Vegan", "Gluten-Free"),
                "Plant-based Asian comfort food, gluten-free.",
                4.8,
                "/images/hana.jpg"
        );

        updateCook(
                cooks.get(2),
                "James' Meal Prep",
                "American",
                List.of("High-Protein", "Dairy-Free"),
                "Weekly meal prep built for people who train hard.",
                4.7,
                "/images/james.jpg"
        );

        cookRepository.saveAll(cooks);

        System.out.println("MealShare demo cooks updated!");
    }

    private void updateCook(
            CookEntity cook,
            String name,
            String cuisine,
            List<String> diets,
            String description,
            double rating,
            String imageUrl) {

        cook.setName(name);
        cook.setCuisine(cuisine);
        cook.setDietaryOptions(diets);
        cook.setDescription(description);
        cook.setRating(rating);
        cook.setImageUrl(imageUrl);

        if (cook.getJoinedDate() == null) {
            cook.setJoinedDate(LocalDate.now());
        }
    }
}
