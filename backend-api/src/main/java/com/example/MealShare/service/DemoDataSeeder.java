package com.example.MealShare.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.MealShare.entity.CookEntity;
import com.example.MealShare.repository.CookRepository;

// component makes this class available to spring
// commandlinerunner runs the code once when the application starts
@Component
public class DemoDataSeeder implements CommandLineRunner {

    private final CookRepository cookRepository;

    // spring injects the repository so this class can read and update cooks
    public DemoDataSeeder(CookRepository cookRepository) {
        this.cookRepository = cookRepository;
    }

    @Override
    public void run(String... args) {

        // loads the existing cook records from the database
        List<CookEntity> cooks = cookRepository.findAll();

        // avoids repeating the update if sofia's profile is already present
        boolean alreadyUpdated = cooks.stream().anyMatch(
                c -> "Sofia's Cocina".equals(c.getName()));

        if (alreadyUpdated) {
            return;
        }

        // this seeder only knows how to update exactly three existing records
        // if the database has a different number, it skips to avoid changing the wrong records
        if (cooks.size() != 3) {
            System.out.println(
                    "Demo seeder skipped: expected exactly 3 cooks.");
            return;
        }

        // sorts records by id so the same record order is used each time
        // note: this assumes the lowest id should become sofia, then hana, then james
        cooks.sort(Comparator.comparing(CookEntity::getId));

        // updates the first existing cook record with sofia's profile information
        updateCook(
                cooks.get(0),
                "Sofia's Cocina",
                "Mexican",
                List.of("Vegetarian"),
                "Bright, veggie-forward Mexican dishes made from scratch.",
                4.9,
                "/images/sofia.jpg"
        );

        // updates the second existing cook record with hana's profile information
        updateCook(
                cooks.get(1),
                "Hana's Kitchen",
                "Asian",
                List.of("Vegan", "Gluten-Free"),
                "Plant-based Asian comfort food, gluten-free.",
                4.8,
                "/images/hana.jpg"
        );

        // updates the third existing cook record with james's profile information
        updateCook(
                cooks.get(2),
                "James' Meal Prep",
                "American",
                List.of("High-Protein", "Dairy-Free"),
                "Weekly meal prep built for people who train hard.",
                4.7,
                "/images/james.jpg"
        );

        // saves all three updated entities to the database
        cookRepository.saveAll(cooks);

        System.out.println("MealShare demo cooks updated!");
    }

    // puts the supplied profile details into an existing cook record
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

        // keeps the original join date, but fills it if the database value is empty
        if (cook.getJoinedDate() == null) {
            cook.setJoinedDate(LocalDate.now());
        }
    }
}