package com.example.MealShare.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// tells jpa that this class represents a customer record in the database
@Entity
@Table(name = "customers")
public class CustomerEntity {

    // database-generated primary key for each customer
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // required customer first name
    @NotBlank(message = "First name is required")
    private String firstName;

    // required customer last name
    @NotBlank(message = "Last name is required")
    private String lastName;

    // email must not be blank, must have email format, and must be unique
    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    @Column(unique = true)
    private String email;

    // settings forms can leave this blank when the customer is not changing it
    // write-only prevents jackson from including the password in json responses
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    // stores multiple dietary preferences in a separate table
    // eager loading means these preferences are loaded with the customer
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "customer_dietary_preferences",
            joinColumns = @JoinColumn(name = "customer_id")
    )
    private List<String> dietaryPreferences = new ArrayList<>();

    // stores multiple cuisine preferences in another separate table
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "customer_cuisine_preferences",
            joinColumns = @JoinColumn(name = "customer_id")
    )
    private List<String> cuisinePreferences = new ArrayList<>();

    // jpa uses the empty constructor when it loads a customer from the database
    public CustomerEntity() {
    }

    // gets and sets the customer id
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    // gets and sets the customer's first name
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    // gets and sets the customer's last name
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    // gets and sets the customer's email
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // gets and sets the password; json output will not expose it
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // gets and sets the customer's dietary preferences
    public List<String> getDietaryPreferences() { return dietaryPreferences; }
    public void setDietaryPreferences(List<String> dietaryPreferences) {
        this.dietaryPreferences = dietaryPreferences == null
                ? new ArrayList<>()
                : new ArrayList<>(dietaryPreferences);
    }

    // gets and sets the customer's cuisine preferences
    public List<String> getCuisinePreferences() { return cuisinePreferences; }
    public void setCuisinePreferences(List<String> cuisinePreferences) {
        this.cuisinePreferences = cuisinePreferences == null
                ? new ArrayList<>()
                : new ArrayList<>(cuisinePreferences);
    }
}