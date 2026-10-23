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

    //US-1: register a new customer
    public CustomerEntity create(CustomerEntity customer) {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
        }
        customer.setId(null);
        return customerRepository.save(customer);
    }

    public List<CustomerEntity> getAll() {
        return customerRepository.findAll();
    }

    //view a customer profile
    public CustomerEntity getById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
    }

    //update profile and preferences
    public CustomerEntity update(Long id, CustomerEntity updated) {
        CustomerEntity existing = getById(id);

        if (customerRepository.existsByEmailAndIdNot(updated.getEmail(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
        }

        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setEmail(updated.getEmail());
        existing.setPassword(updated.getPassword());
        existing.setDietaryPreferences(updated.getDietaryPreferences());
        existing.setCuisinePreferences(updated.getCuisinePreferences());
        return customerRepository.save(existing);
    }

    public void delete(Long id) {
        CustomerEntity existing = getById(id);
        customerRepository.delete(existing);
    }
}