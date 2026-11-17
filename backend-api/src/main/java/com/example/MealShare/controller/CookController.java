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
import org.springframework.web.bind.annotation.RestController;

import com.example.MealShare.entity.CookEntity;
import com.example.MealShare.service.CookService;

// marks this class as a rest api controller that returns data instead of html pages
@RestController
// all routes in this controller start with /api/cooks
@RequestMapping("/api/cooks")
public class CookController {

    // the controller sends requests to the service instead of handling database work itself
    private final CookService cookService;

    // spring automatically provides the cook service when it creates this controller
    public CookController(CookService cookService) {
        this.cookService = cookService;
    }

    // gets every cook that matches the requested cuisine
    // example: GET /api/cooks/cuisine/Asian
    @GetMapping("/cuisine/{cuisine}")
    public List<CookEntity> getCooksByCuisine(@PathVariable String cuisine) {
        return cookService.getCooksByCuisine(cuisine);
    }

    // gets one cook using their database id
    // example: GET /api/cooks/1
    @GetMapping("/{id}")
    public CookEntity getCook(@PathVariable Long id) {
        return cookService.getCookById(id);
    }

    // creates a new cook from the json data sent in the request body
    // returns status 201 to show that the record was created
    @PostMapping
    public ResponseEntity<CookEntity> createCook(@RequestBody CookEntity cook) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cookService.createCook(cook));
    }

    // updates the cook with this id using the json data in the request body
    @PutMapping("/{id}")
    public CookEntity updateCook(
            @PathVariable Long id,
            @RequestBody CookEntity cook) {
        return cookService.updateCook(id, cook);
    }

    // deletes the cook with this id and returns status 204 when successful
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCook(@PathVariable Long id) {
        cookService.deleteCook(id);
        return ResponseEntity.noContent().build();
    }
}