package com.example.MealShare.controller;

import com.example.MealShare.entity.Cook;
import com.example.MealShare.service.CookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cooks")
public class CookController {

    private final CookService cookService;

    public CookController(CookService cookService) {
        this.cookService = cookService;
    }

    // GET /api/cooks, /api/cooks?cuisine=Italian, /api/cooks?name=mar
    @GetMapping
    public List<Cook> getCooks(@RequestParam(required = false) String name,
                               @RequestParam(required = false) String cuisine) {
        return cookService.getCooks(name, cuisine);
    }

    @GetMapping("/{id}")
    public Cook getCook(@PathVariable long id) {
        return cookService.getCookById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cook createCook(@Valid @RequestBody Cook cook) {
        return cookService.createCook(cook);
    }

    @PutMapping("/{id}")
    public Cook updateCook(@PathVariable long id, @Valid @RequestBody Cook cook) {
        return cookService.updateCook(id, cook);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCook(@PathVariable long id) {
        cookService.deleteCook(id);
    }
}