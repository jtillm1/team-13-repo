package com.example.MealShare.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.MealShare.entity.CustomerEntity;
import com.example.MealShare.service.CustomerService;

import jakarta.validation.Valid;

// handles customer api requests and returns customer data as json
@RestController
// every route in this class starts with /api/customers
@RequestMapping("/api/customers")
public class CustomerController {

    // the service handles the customer rules and database operations
    private final CustomerService customerService;

    // spring provides the customer service when it creates this controller
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // creates a customer from the json request body
    // @Valid runs the validation rules written in CustomerEntity
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerEntity create(@Valid @RequestBody CustomerEntity customer) {
        return customerService.create(customer);
    }

    // returns all customer records
    @GetMapping
    public List<CustomerEntity> getAll() {
        return customerService.getAll();
    }

    // returns one customer using the id in the url
    @GetMapping("/{id}")
    public CustomerEntity getById(@PathVariable Long id) {
        return customerService.getById(id);
    }

    // updates the customer with this id using the json request body
    @PutMapping("/{id}")
    public CustomerEntity update(
            @PathVariable Long id,
            @Valid @RequestBody CustomerEntity customer) {
        return customerService.update(id, customer);
    }

    // deletes the customer with this id and returns status 204 when successful
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        customerService.delete(id);
    }
}