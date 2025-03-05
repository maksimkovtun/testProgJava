package com.program.testProgJava.rest;

import com.program.testProgJava.dao.entities.StoresEntity;
import com.program.testProgJava.dao.repositories.StoresRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Stores", description = "Сервис для управления магазинами")
@RequestMapping("/store/api/stores")
public class StoresController {

    private final StoresRepository storesRepository;

    public StoresController(StoresRepository storesRepository) {
        this.storesRepository = storesRepository;
    }

    @GetMapping
    @Operation(summary = "Получить список всех магазинов")
    public List<StoresEntity> getAllStores() {
        return storesRepository.findAll();
    }
}
