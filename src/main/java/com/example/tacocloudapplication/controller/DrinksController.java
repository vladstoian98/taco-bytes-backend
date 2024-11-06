package com.example.tacocloudapplication.controller;

import com.example.tacocloudapplication.service.DrinksService;
import com.example.tacocloudapplication.table.Drink;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/drinks")
//@CrossOrigin(origins = {"https://localhost:4200", "http://3.79.153.161"})
public class DrinksController {
    private final DrinksService drinksService;

    @Autowired
    public DrinksController(DrinksService drinksService) {
        this.drinksService = drinksService;
    }

    @GetMapping("/selection")
    public ResponseEntity<List<Drink>> getAllDrinks() {
        List<Drink> drinks = this.drinksService.findAllDrinks();

        if(drinks.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(drinks);
    }

}
