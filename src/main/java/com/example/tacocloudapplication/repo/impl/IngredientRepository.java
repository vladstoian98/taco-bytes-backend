package com.example.tacocloudapplication.repo.impl;

import com.example.tacocloudapplication.table.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, String> {

    @Transactional
    @Query(value = "select * from ingredient", nativeQuery = true)
    List<Ingredient> findAllIngredients();
}
