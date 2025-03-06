package com.program.testProgJava.rest;

import com.program.testProgJava.dao.entities.PurchasesEntity;
import com.program.testProgJava.dao.repositories.PurchasesRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    public PurchasesController(PurchasesRepository purchasesRepository) {
        this.purchasesRepository = purchasesRepository;
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

    @GetMapping("/summary")
    @Operation(summary = "Общая информация о покупках")
    public ResponseEntity<List<PurchasesEntity>> getPurchasesSummary() {
        List<PurchasesEntity> purchases = purchasesRepository.findAll();
        return ResponseEntity.ok(purchases);
    }
}

