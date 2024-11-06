package com.example.tacocloudapplication.controller;

import com.example.tacocloudapplication.repo.impl.IngredientRepository;
import com.example.tacocloudapplication.repo.impl.TacoRepository;
import com.example.tacocloudapplication.repo.impl.UserRepository;
import com.example.tacocloudapplication.table.Ingredient;
import com.example.tacocloudapplication.table.Ingredient.Type;
import com.example.tacocloudapplication.table.Taco;
import com.example.tacocloudapplication.table.TacoOrder;
import com.example.tacocloudapplication.table.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
//@CrossOrigin(origins = {"https://localhost:4200", "http://3.79.153.161"})
public class DesignTacoController {

    private final IngredientRepository ingredientRepository;

    private final TacoRepository tacoRepository;

    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepository, TacoRepository tacoRepository,
                                UserRepository userRepository, EntityManager entityManager) {
        this.ingredientRepository = ingredientRepository;
        this.tacoRepository = tacoRepository;
        this.userRepository = userRepository;
        this.entityManager = entityManager;
    }

    @GetMapping("/taco")
    public ResponseEntity<Map<Type, List<Ingredient>>> getIngredientsGroupedByType() {
        List<Ingredient> ingredients = ingredientRepository.findAllIngredients();

        if (ingredients.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Map<Type, List<Ingredient>> groupedIngredients = ingredients.stream()
                .collect(Collectors.groupingBy(Ingredient::getType));

        return ResponseEntity.ok(groupedIngredients);
    }


    @PostMapping("/taco")
    @Transactional
    public ResponseEntity<Taco> createTacoFromIngredients(@Valid @RequestBody Taco taco,
                                                          Principal principal) {
        if (taco == null) {
            return ResponseEntity.badRequest().build();
        }

        User currentUser = userRepository.findByUsername(principal.getName());

        taco.setCreatedByUserId(currentUser.getId());

        for (int i = 0; i < taco.getIngredients().size(); i++) {
            Ingredient ingredient = taco.getIngredients().get(i);
            taco.setTotalTacoPrice(taco.getTotalTacoPrice() + ingredient.getPrice());
            Ingredient managedIngredient = ingredientRepository.findById(ingredient.getId()).orElse(null);
            if (managedIngredient != null) {
                taco.getIngredients().set(i, managedIngredient);
                managedIngredient.addTaco(taco);
            }
        }

        taco.setTotalTacoPrice(roundToOneDecimal(taco.getTotalTacoPrice()));

        Taco savedTaco = tacoRepository.save(taco);
        entityManager.flush();
        entityManager.clear();

        return ResponseEntity.ok(savedTaco);
    }

    @GetMapping("/emptyTaco")
    public ResponseEntity<Taco> getTaco() {
        return ResponseEntity.ok(new Taco());
    }

    private float roundToOneDecimal(float value) {
        return Math.round(value * 10) / 10.0f;
    }

}
