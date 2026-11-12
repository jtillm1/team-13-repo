
package com.example.MealShare.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.MealShare.entity.CustomerEntity;
import com.example.MealShare.repository.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // register a customer
    public CustomerEntity create(CustomerEntity customer) {
        checkPassword(customer.getPassword());

        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "That email is already registered"
            );
        }

        customer.setId(null);
        return customerRepository.save(customer);
    }

    // log in a customer
    public CustomerEntity login(String email, String password) {
        return customerRepository.findByEmail(email)
            .filter(c -> c.getPassword() != null
                && c.getPassword().equals(password))
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Invalid email or password"
            ));
    }

    // get all customers
    public List<CustomerEntity> getAll() {
        return customerRepository.findAll();
    }

    // get one customer
    public CustomerEntity getById(Long id) {
        return customerRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Customer not found"
            ));
    }

    // update customer settings
    public CustomerEntity update(Long id, CustomerEntity updated) {
        CustomerEntity existing = getById(id);

        if (!existing.getEmail().equals(updated.getEmail())
                && customerRepository.existsByEmail(updated.getEmail())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "That email is already registered"
            );
        }

        // only update password if a new one was entered
        if (updated.getPassword() != null
                && !updated.getPassword().isBlank()) {
            checkPassword(updated.getPassword());
            existing.setPassword(updated.getPassword());
        }

        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setEmail(updated.getEmail());
        existing.setDietaryPreferences(updated.getDietaryPreferences());
        existing.setCuisinePreferences(updated.getCuisinePreferences());

        return customerRepository.save(existing);
    }

    // delete customer
    public void delete(Long id) {
        customerRepository.delete(getById(id));
    }

    // check password length
    private void checkPassword(String password) {
        if (password == null || password.length() < 6) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Password must be at least 6 characters"
            );
        }
    }
}