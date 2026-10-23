package com.example.MealShare.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @NotBlank(message = "email is required")
    @Email(message = "email must be valid")
    @Column(unique = true)
    private String email;

    // WRITE_ONLY: accepted in requests, never returned in responses
    @NotBlank(message = "password is required")
    @Size(min = 8, message = "password must be at least 8 characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    // e.g. Italian, Mexican, American, Asian, Mediterranean
    private String cuisine;

    @DecimalMin(value = "0.0", message = "rating must be between 0 and 5")
    @DecimalMax(value = "5.0", message = "rating must be between 0 and 5")
    private Double rating;

    private LocalDate joinedDate;

    // The cook's menu. READ_ONLY: shown in responses, managed through /api/cooks/{id}/meals.
    // Deleting a cook also deletes their meals.
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToMany(mappedBy = "cook", cascade = CascadeType.REMOVE)
    private List<Meal> meals = new ArrayList<>();

    public Cook() {
    }

    public long getCookId() { return cookId; }
    public void setCookId(long cookId) { this.cookId = cookId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getCuisine() { return cuisine; }
    public void setCuisine(String cuisine) { this.cuisine = cuisine; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public LocalDate getJoinedDate() { return joinedDate; }
    public void setJoinedDate(LocalDate joinedDate) { this.joinedDate = joinedDate; }

    public List<Meal> getMeals() { return meals; }
    public void setMeals(List<Meal> meals) { this.meals = meals; }
}