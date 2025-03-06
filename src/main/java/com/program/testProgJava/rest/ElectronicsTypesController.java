package com.program.testProgJava.rest;

import com.program.testProgJava.dao.entities.ElectronicsTypesEntity;
import com.program.testProgJava.dao.repositories.ElectronicsTypesRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Electronics Types", description = "Сервис для управления типами электроники")
@RequestMapping("/store/api/electronics/types")
public class ElectronicsTypesController {

    private final ElectronicsTypesRepository electronicsTypesRepository;

    public ElectronicsTypesController(ElectronicsTypesRepository electronicsTypesRepository) {
        this.electronicsTypesRepository = electronicsTypesRepository;
    }

    @GetMapping
    @Operation(summary = "Получить все типы электроники")
    public List<ElectronicsTypesEntity> getAllElectronicsTypes() {
        return electronicsTypesRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить тип электроники по ID")
    public ResponseEntity<ElectronicsTypesEntity> getElectronicsTypeById(@PathVariable Long id) {
        Optional<ElectronicsTypesEntity> electronicsType = electronicsTypesRepository.findById(id);
        return electronicsType.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    @Operation(summary = "Добавить новый тип электроники")
    public ResponseEntity<ElectronicsTypesEntity> addElectronicsType(@RequestBody ElectronicsTypesEntity electronicsType) {
        ElectronicsTypesEntity savedElectronicsType = electronicsTypesRepository.save(electronicsType);
        return ResponseEntity.ok(savedElectronicsType);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Обновить тип электроники")
    public ResponseEntity<ElectronicsTypesEntity> updateElectronicsType(@PathVariable Long id, @RequestBody ElectronicsTypesEntity updatedElectronicsType) {
        return electronicsTypesRepository.findById(id)
                .map(existingElectronicsType -> {
                    existingElectronicsType.setName(updatedElectronicsType.getName());
                    electronicsTypesRepository.save(existingElectronicsType);
                    return ResponseEntity.ok(existingElectronicsType);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Удалить тип электроники")
    public ResponseEntity<Void> deleteElectronicsType(@PathVariable Long id) {
        if (electronicsTypesRepository.existsById(id)) {
            electronicsTypesRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
