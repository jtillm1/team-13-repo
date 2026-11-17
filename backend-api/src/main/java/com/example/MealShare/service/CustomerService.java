package com.example.MealShare.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.MealShare.entity.CustomerEntity;
import com.example.MealShare.repository.CustomerRepository;

// service contains the customer rules used by both the website and the api
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    // spring injects the repository so this service can access customer records
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // registers a new customer
    public CustomerEntity create(CustomerEntity customer) {

        // checks that the password meets the minimum length
        checkPassword(customer.getPassword());

        // prevents duplicate email addresses
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "That email is already registered"
            );
        }

        // clears any id supplied by the request so the database creates a new one
        customer.setId(null);
        return customerRepository.save(customer);
    }

    // checks a customer's email and password during login
    public CustomerEntity login(String email, String password) {
        return customerRepository.findByEmail(email)
                // only accepts the record if it has a password matching the submitted one
                .filter(c -> c.getPassword() != null
                        && c.getPassword().equals(password))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid email or password"
                ));
    }

    // returns every customer record
    public List<CustomerEntity> getAll() {
        return customerRepository.findAll();
    }

    // finds one customer by id or returns a 404 error
    public CustomerEntity getById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Customer not found"
                ));
    }

    // updates the customer's account settings
    public CustomerEntity update(Long id, CustomerEntity updated) {
        // loads the existing record so its id and stored values are preserved
        CustomerEntity existing = getById(id);

        // only check for a duplicate email if the customer changed their email
        if (!existing.getEmail().equals(updated.getEmail())
                && customerRepository.existsByEmail(updated.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "That email is already registered"
            );
        }

        // only update the password if the customer entered a new one
        if (updated.getPassword() != null
                && !updated.getPassword().isBlank()) {
            checkPassword(updated.getPassword());
            existing.setPassword(updated.getPassword());
        }

        // copies the remaining editable fields into the existing customer
        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setEmail(updated.getEmail());
        existing.setDietaryPreferences(updated.getDietaryPreferences());
        existing.setCuisinePreferences(updated.getCuisinePreferences());

        return customerRepository.save(existing);
    }

    // deletes a customer after looking up the record
    public void delete(Long id) {
        customerRepository.delete(getById(id));
    }

    // makes sure a password is present and has at least six characters
    private void checkPassword(String password) {
        if (password == null || password.length() < 6) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password must be at least 6 characters"
            );
        }
    }
}