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

import com.example.MealShare.entity.Cook;
import com.example.MealShare.service.CookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cooks")
public class CookController {

    private final CookService cookService;

    public CookController(CookService cookService) {
        this.cookService = cookService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cook create(@Valid @RequestBody Cook cook) {
        return cookService.create(cook);
    }

    @GetMapping
    public List<Cook> getAll() {
        return cookService.getAll();
    }

    @GetMapping("/{id}")
    public Cook getById(@PathVariable Long id) {
        return cookService.getById(id);
    }

    @PutMapping("/{id}")
    public Cook update(@PathVariable Long id, @Valid @RequestBody Cook cook) {
        return cookService.update(id, cook);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        cookService.delete(id);
    }
}