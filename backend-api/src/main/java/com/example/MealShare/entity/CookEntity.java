package com.example.MealShare.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

// tells jpa that this java class represents a database entity
@Entity
// connects this entity to the cooks table in postgresql
@Table(name = "cooks")
public class CookEntity {

    // primary key generated automatically when a cook is inserted
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // basic cook profile information stored as columns in the cooks table
    private String name;
    private String description;

    // each cook must have a unique email address in the database
    @Column(unique = true)
    private String email;

    private String password;
    private String cuisine;

    // one cook can have multiple dietary options, stored in a separate table
    @ElementCollection
    @CollectionTable(
            name = "cook_dietary_options",
            joinColumns = @JoinColumn(name = "cook_id")
    )
    @Column(name = "dietary_option")
    private List<String> dietaryOptions = new ArrayList<>();

    // wrapper type Double allows the database value to be null
    private Double rating;
    private String imageUrl;
    private LocalDate joinedDate;

    // jpa needs an empty constructor when it loads a cook from the database
    public CookEntity() {
    }

    // returns the cook's database id
    public Long getId() {
        return id;
    }

    // sets the cook's database id
    public void setId(Long id) {
        this.id = id;
    }

    // returns the cook's name
    public String getName() {
        return name;
    }

    // changes the cook's name
    public void setName(String name) {
        this.name = name;
    }

    // returns the cook's description
    public String getDescription() {
        return description;
    }

    // changes the cook's description
    public void setDescription(String description) {
        this.description = description;
    }

    // returns the cook's email
    public String getEmail() {
        return email;
    }

    // changes the cook's email
    public void setEmail(String email) {
        this.email = email;
    }

    // returns the cook's password
    public String getPassword() {
        return password;
    }

    // changes the cook's password
    public void setPassword(String password) {
        this.password = password;
    }

    // returns the cook's cuisine
    public String getCuisine() {
        return cuisine;
    }

    // changes the cook's cuisine
    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    // returns the list of dietary options for this cook
    public List<String> getDietaryOptions() {
        return dietaryOptions;
    }

    // safely copies the supplied list so the entity keeps its own list
    public void setDietaryOptions(List<String> dietaryOptions) {
        this.dietaryOptions = dietaryOptions == null
                ? new ArrayList<>()
                : new ArrayList<>(dietaryOptions);
    }

    // returns the cook's rating
    public Double getRating() {
        return rating;
    }

    // changes the cook's rating
    public void setRating(Double rating) {
        this.rating = rating;
    }

    // returns the profile image path
    public String getImageUrl() {
        return imageUrl;
    }

    // changes the profile image path
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // returns the date the cook joined
    public LocalDate getJoinedDate() {
        return joinedDate;
    }

    // changes the date the cook joined
    public void setJoinedDate(LocalDate joinedDate) {
        this.joinedDate = joinedDate;
    }
}