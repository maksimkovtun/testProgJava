package com.program.testProgJava.rest;

import com.program.testProgJava.dao.entities.StoresEntity;
import com.program.testProgJava.dao.repositories.StoresRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/add")
    @Operation(summary = "Добавить новый магазин")
    public ResponseEntity<StoresEntity> addStore(@RequestBody StoresEntity storesEntity) {
        StoresEntity savedStore = storesRepository.save(storesEntity);
        return ResponseEntity.ok(savedStore);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Обновить магазин")
    public ResponseEntity<StoresEntity> updateStore(@PathVariable Long id, @RequestBody StoresEntity updatedStore) {
        return storesRepository.findById(id)
                .map(existingStore -> {
                    existingStore.setName(updatedStore.getName());
                    existingStore.setAddress(updatedStore.getAddress());
                    storesRepository.save(existingStore);
                    return ResponseEntity.ok(existingStore);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Удалить магазин")
    public ResponseEntity<Void> deleteStore(@PathVariable Long id) {
        if (storesRepository.existsById(id)) {
            storesRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
