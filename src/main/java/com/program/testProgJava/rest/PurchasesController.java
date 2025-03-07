package com.program.testProgJava.rest;

import com.program.testProgJava.dao.entities.ElectronicsProductsEntity;
import com.program.testProgJava.dao.entities.PurchasesEntity;
import com.program.testProgJava.dao.repositories.ElectronicsProductsRepository;
import com.program.testProgJava.dao.repositories.PurchasesRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@RestController
@Tag(name = "Purchases", description = "Сервис для управления покупками")
@RequestMapping("/store/api/purchases")
public class PurchasesController {

    private final PurchasesRepository purchasesRepository;
    private final ElectronicsProductsRepository electronicsProductsRepository;

    public PurchasesController(PurchasesRepository purchasesRepository, ElectronicsProductsRepository electronicsProductsRepository) {
        this.purchasesRepository = purchasesRepository;
        this.electronicsProductsRepository = electronicsProductsRepository;
    }

    @GetMapping
    @Operation(summary = "Общая информация о покупках")
    public ResponseEntity<List<PurchasesEntity>> getPurchasesSummary() {
        List<PurchasesEntity> purchases = purchasesRepository.findAll();
        return ResponseEntity.ok(purchases);
    }

    @PostMapping("/add")
    @Operation(summary = "Добавить новую покупку")
    public ResponseEntity<?> addPurchase(@RequestBody PurchasesEntity purchase) {
        ElectronicsProductsEntity product = electronicsProductsRepository.findById(purchase.getProductId())
                .orElseThrow(() -> new RuntimeException("Товар не найден"));

        if (product.getQuantity() == 0 || product.getArchived()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Товар отсутствует в наличии.");
        }
        PurchasesEntity savedPurchase = purchasesRepository.save(purchase);
        return ResponseEntity.ok(savedPurchase);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Обновить покупку")
    public ResponseEntity<PurchasesEntity> updatePurchase(@PathVariable Long id, @RequestBody PurchasesEntity updatedPurchase) {
        return purchasesRepository.findById(id)
                .map(existingPurchase -> {
                    existingPurchase.setProductId(updatedPurchase.getProductId());
                    existingPurchase.setEmployeeId(updatedPurchase.getEmployeeId());
                    existingPurchase.setPurchaseDate(updatedPurchase.getPurchaseDate());
                    existingPurchase.setPurchaseTypeId(updatedPurchase.getPurchaseTypeId());
                    existingPurchase.setStoreId(updatedPurchase.getStoreId());
                    purchasesRepository.save(existingPurchase);
                    return ResponseEntity.ok(existingPurchase);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Удалить покупку")
    public ResponseEntity<Void> deletePurchase(@PathVariable Long id) {
        if (purchasesRepository.existsById(id)) {
            purchasesRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/total-cash")
    @Operation(summary = "Общая сумма покупок за наличные")
    public Double getTotalCashPurchases() {
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        Long cashTypeId = 1L;
        return purchasesRepository.findTotalCashSales(Date.valueOf(oneYearAgo), cashTypeId);
    }

    @GetMapping("/total-sales")
    @Operation(summary = "Общая сумма всех покупок")
    public Double getTotalSales() {
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        return purchasesRepository.findTotalSales(Date.valueOf(oneYearAgo));
    }
}

