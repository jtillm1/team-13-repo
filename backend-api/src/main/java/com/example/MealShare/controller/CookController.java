package com.example.MealShare.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.MealShare.entity.CookEntity;
import com.example.MealShare.service.CookService;

@RestController
@RequestMapping("/api/cooks")
public class CookController {

    private final CookService cookService;

    public CookController(CookService cookService) {
        this.cookService = cookService;
    }

    // GET /api/cooks  or  GET /api/cooks?cuisine=Asian
    @GetMapping
    public List<CookEntity> getCooks(@RequestParam(required = false) String cuisine) {
        if (cuisine != null && !cuisine.isBlank()) {
            return cookService.getCooksByCuisine(cuisine);
        }
        return cookService.getAllCooks();
    }

    // GET /api/cooks/1
    @GetMapping("/{id}")
    public CookEntity getCook(@PathVariable Long id) {
        return cookService.getCookById(id);
    }

    // POST /api/cooks
    @PostMapping
    public ResponseEntity<CookEntity> createCook(@RequestBody CookEntity cook) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cookService.createCook(cook));
    }

    // PUT /api/cooks/1
    @PutMapping("/{id}")
    public CookEntity updateCook(@PathVariable Long id, @RequestBody CookEntity cook) {
        return cookService.updateCook(id, cook);
    }

    // DELETE /api/cooks/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCook(@PathVariable Long id) {
        cookService.deleteCook(id);
        return ResponseEntity.noContent().build();
    }
}