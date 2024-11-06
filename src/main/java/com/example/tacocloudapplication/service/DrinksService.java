package com.example.tacocloudapplication.service;

import com.example.tacocloudapplication.repo.impl.DrinkRepository;
import com.example.tacocloudapplication.table.Drink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Service
public class DrinksService {
    private final DrinkRepository drinkRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public DrinksService(DrinkRepository drinkRepository, EntityManager entityManager) {
        this.drinkRepository = drinkRepository;
        this.entityManager = entityManager;
    }

    public List<Drink> findAllDrinks() {
        return this.drinkRepository.findAllDrinks();
    }
}
