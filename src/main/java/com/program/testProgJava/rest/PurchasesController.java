package com.program.testProgJava.rest;

import com.program.testProgJava.dao.repositories.PurchasesRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

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
        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);
        Long cashTypeId = 1L;
        return purchasesRepository.findTotalCashSales(oneYearAgo, cashTypeId);
    }

    @GetMapping("/total-sales")
    @Operation(summary = "Общая сумма всех покупок")
    public Double getTotalSales() {
        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);
        return purchasesRepository.findTotalSales(oneYearAgo);
    }
}
