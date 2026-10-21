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

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerEntity create(@Valid @RequestBody CustomerEntity customer) {
        return customerService.create(customer);
    }

    @GetMapping
    public List<CustomerEntity> getAll() {
        return customerService.getAll();
    }

    @GetMapping("/{id}")
    public CustomerEntity getById(@PathVariable Long id) {
        return customerService.getById(id);
    }

    @PutMapping("/{id}")
    public CustomerEntity update(@PathVariable Long id, @Valid @RequestBody CustomerEntity customer) {
        return customerService.update(id, customer);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        customerService.delete(id);
    }
}