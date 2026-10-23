package com.example.MealShare.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meals")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long mealId;

    @NotBlank(message = "name is required")
    private String name;

    @Column(length = 1000)
    private String description;

    // Entree, Side, Dessert, Family Pack, Beverage
    private String category;

    @NotNull(message = "price is required")
    @DecimalMin(value = "0.01", message = "price must be greater than 0")
    @Digits(integer = 6, fraction = 2, message = "price must have at most 2 decimal places")
    @Column(precision = 8, scale = 2)
    private BigDecimal price;

    // e.g. Vegetarian, Vegan, Gluten-Free, Dairy-Free, High-Protein
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> dietaryTags = new ArrayList<>();

    // Owner of this meal. Hidden from JSON; the API exposes cookId instead.
    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "cook_id", nullable = false)
    private Cook cook;

    public Meal() {
    }

    public long getMealId() { return mealId; }
    public void setMealId(long mealId) { this.mealId = mealId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public List<String> getDietaryTags() { return dietaryTags; }
    public void setDietaryTags(List<String> dietaryTags) {
        this.dietaryTags = dietaryTags == null ? new ArrayList<>() : new ArrayList<>(dietaryTags);
    }

    public Cook getCook() { return cook; }
    public void setCook(Cook cook) { this.cook = cook; }

    @JsonProperty(value = "cookId", access = JsonProperty.Access.READ_ONLY)
    public Long getCookId() {
        return cook == null ? null : cook.getCookId();
    }
}