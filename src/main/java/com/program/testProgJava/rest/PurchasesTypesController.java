package com.program.testProgJava.rest;

import com.program.testProgJava.dao.entities.PurchaseTypesEntity;
import com.program.testProgJava.dao.repositories.PurchaseTypesRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/store/api/purchases/types")
public class PurchasesTypesController {

    private final PurchaseTypesRepository purchaseTypesRepository;

    public PurchasesTypesController(PurchaseTypesRepository purchaseTypesRepository) {
        this.purchaseTypesRepository = purchaseTypesRepository;
    }

    @GetMapping
    public List<PurchaseTypesEntity> getAllPurchaseTypes() {
        return purchaseTypesRepository.findAll();
    }

    @PostMapping("/add")
    @Operation(summary = "Добавить новый тип оплаты")
    public ResponseEntity<PurchaseTypesEntity> addPurchaseType(@RequestBody PurchaseTypesEntity purchaseType) {
        PurchaseTypesEntity savedPurchaseType = purchaseTypesRepository.save(purchaseType);
        return ResponseEntity.ok(savedPurchaseType);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Обновить тип оплаты")
    public ResponseEntity<PurchaseTypesEntity> updatePurchaseType(@PathVariable Long id, @RequestBody PurchaseTypesEntity updatedPurchaseType) {
        return purchaseTypesRepository.findById(id)
                .map(existingPurchaseType -> {
                    existingPurchaseType.setName(updatedPurchaseType.getName());
                    purchaseTypesRepository.save(existingPurchaseType);
                    return ResponseEntity.ok(existingPurchaseType);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Удалить тип оплаты")
    public ResponseEntity<Void> deletePurchaseType(@PathVariable Long id) {
        if (purchaseTypesRepository.existsById(id)) {
            purchaseTypesRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
