package com.example.MealShare.service;

import com.example.MealShare.entity.CookEntity;
import com.example.MealShare.repository.CookRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DemoDataSeeder implements CommandLineRunner {

    private final CookRepository cookRepository;

    public DemoDataSeeder(CookRepository cookRepository) {
        this.cookRepository = cookRepository;
    }

    @Override
    public void run(String... args) {
        if (cookRepository.count() > 0) {
            return;
        }
        cookRepository.save(cook("Maria's Kitchen", "maria@mealshare.demo", "Italian",
                List.of("Vegetarian", "Gluten-Free"),
                "Home-style Italian cooking made with fresh, seasonal ingredients.", 4.8, "/images/maria.jpg"));
        cookRepository.save(cook("James' Meal Prep", "james@mealshare.demo", "American",
                List.of("High-Protein", "Dairy-Free"),
                "Weekly meal prep built for people who train hard.", 4.7, "/images/james.jpg"));
        cookRepository.save(cook("Sofia's Cocina", "sofia@mealshare.demo", "Mexican",
                List.of("Vegetarian"),
                "Bright, veggie-forward Mexican dishes made from scratch.", 4.9, "/images/sofia.jpg"));
        cookRepository.save(cook("Hana's Kitchen", "hana@mealshare.demo", "Asian",
                List.of("Vegan", "Gluten-Free"),
                "Plant-based Asian comfort food, gluten-free.", 4.8, "/images/hana.jpg"));
    }

    private CookEntity cook(String name, String email, String cuisine, List<String> diets,
                            String bio, double rating, String imageUrl) {
        CookEntity c = new CookEntity();
        c.setBusinessName(name);
        c.setEmail(email);
        c.setPassword("demo1234");
        c.setCuisine(cuisine);
        c.setDietaryOptions(new ArrayList<>(diets));
        c.setBio(bio);
        c.setRating(rating);
        c.setImageUrl(imageUrl);
        return c;
    }
}