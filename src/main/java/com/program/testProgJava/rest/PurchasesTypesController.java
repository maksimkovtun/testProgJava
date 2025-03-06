package com.program.testProgJava.rest;

import com.program.testProgJava.dao.entities.PurchaseTypesEntity;
import com.program.testProgJava.dao.repositories.PurchaseTypesRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/store/api/purchase-types")
public class PurchasesTypesController {

    private final PurchaseTypesRepository purchaseTypesRepository;

    public PurchasesTypesController(PurchaseTypesRepository purchaseTypesRepository) {
        this.purchaseTypesRepository = purchaseTypesRepository;
    }

    @GetMapping
    public List<PurchaseTypesEntity> getAllPurchaseTypes() {
        return purchaseTypesRepository.findAll();
    }
}
