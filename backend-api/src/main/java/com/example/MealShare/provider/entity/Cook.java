package com.example.MealShare.provider.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@Entity
@Table(name = "cooks")
public class Cook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cookId;

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "description is required")
    @Column(length = 1000)
    private String description;

    // e.g. Italian, Mexican, American, Asian, Mediterranean
    private String cuisine;

    @DecimalMin(value = "0.0", message = "rating must be between 0 and 5")
    @DecimalMax(value = "5.0", message = "rating must be between 0 and 5")
    private Double rating;

    private LocalDate joinedDate;

    public Cook() {
    }

    public long getCookId() { return cookId; }
    public void setCookId(long cookId) { this.cookId = cookId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCuisine() { return cuisine; }
    public void setCuisine(String cuisine) { this.cuisine = cuisine; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public LocalDate getJoinedDate() { return joinedDate; }
    public void setJoinedDate(LocalDate joinedDate) { this.joinedDate = joinedDate; }
}