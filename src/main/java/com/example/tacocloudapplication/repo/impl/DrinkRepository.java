package com.example.tacocloudapplication.repo.impl;

import com.example.tacocloudapplication.table.Drink;
import com.example.tacocloudapplication.table.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface DrinkRepository extends JpaRepository<Drink, Long> {
    @Query(value = "select * from drink", nativeQuery = true)
    List<Drink> findAllDrinks();

    @Transactional
    @Modifying
    @Query(value = "delete from taco_order_drinks where order_id = :order_id", nativeQuery = true)
    void deleteFromTacoOrderDrinksByOrderId(@Param("order_id") Long order_id);
}
