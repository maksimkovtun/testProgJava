package com.program.testProgJava.rest;

import com.program.testProgJava.dao.entities.ElectronicsProductsEntity;
import com.program.testProgJava.dao.repositories.ElectronicsProductsRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Electronics", description = "Сервис для управления электроникой")
@RequestMapping("/store/api/electronics")
public class ElectronicsProductsController {

    private final ElectronicsProductsRepository electronicsProductsRepository;

    public ElectronicsProductsController(ElectronicsProductsRepository electronicsProductsRepository) {
        this.electronicsProductsRepository = electronicsProductsRepository;
    }

    @GetMapping
    @Operation(summary = "Получить все товары")
    public List<ElectronicsProductsEntity> getAllProducts() {
        return electronicsProductsRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить товар электроники по ID")
    public ResponseEntity<ElectronicsProductsEntity> getElectronicsProductById(@PathVariable Long id) {
        Optional<ElectronicsProductsEntity> product = electronicsProductsRepository.findById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    @Operation(summary = "Добавить новый товар электроники")
    public ResponseEntity<ElectronicsProductsEntity> addElectronicsProduct(@RequestBody ElectronicsProductsEntity product) {
        ElectronicsProductsEntity savedProduct = electronicsProductsRepository.save(product);
        return ResponseEntity.ok(savedProduct);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Обновить товар электроники")
    public ResponseEntity<ElectronicsProductsEntity> updateElectronicsProduct(@PathVariable Long id, @RequestBody ElectronicsProductsEntity updatedProduct) {
        return electronicsProductsRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(updatedProduct.getName());
                    existingProduct.setElectronicsTypeId(updatedProduct.getElectronicsTypeId());
                    existingProduct.setPrice(updatedProduct.getPrice());
                    existingProduct.setQuantity(updatedProduct.getQuantity());
                    existingProduct.setArchived(updatedProduct.getArchived());
                    existingProduct.setDescription(updatedProduct.getDescription());
                    electronicsProductsRepository.save(existingProduct);
                    return ResponseEntity.ok(existingProduct);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Удалить товар электроники")
    public ResponseEntity<Void> deleteElectronicsProduct(@PathVariable Long id) {
        if (electronicsProductsRepository.existsById(id)) {
            electronicsProductsRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
