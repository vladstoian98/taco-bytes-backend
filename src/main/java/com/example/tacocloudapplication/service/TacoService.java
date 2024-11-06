package com.example.tacocloudapplication.service;

import com.example.tacocloudapplication.repo.impl.TacoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
public class TacoService {
    private final TacoRepository tacoRepository;

    public TacoService(TacoRepository tacoRepository) {
        this.tacoRepository = tacoRepository;
    }

    public void deleteTacosWithoutOrder() {
        tacoRepository.deleteOrderlessTacosEntitiesFromIngredientTacosTable();
        tacoRepository.deleteOrderlessTacoFromTacoTable();
    }

    public void deleteSelectedTacoFromDatabase(Long tacoId) {
        tacoRepository.deleteEntriesFromIngredientTaco(tacoId);
    }
}
