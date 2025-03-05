package com.program.testProgJava.rest;

import com.program.testProgJava.dao.entities.EmployeesEntity;
import com.program.testProgJava.dao.repositories.EmployeesRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.util.List;

@RestController
@Tag(name = "Employee", description = "Сервис для управления сотрудниками магазина")
@RequestMapping("/store/api/employees")
public class EmployeesController {

    private final EmployeesRepository employeesRepository;

    public EmployeesController(EmployeesRepository employeesRepository) {
        this.employeesRepository = employeesRepository;
    }

    @GetMapping("/best/sales-count")
    @Operation(summary = "Лучшие сотрудники по количеству продаж")
    public ResponseEntity<List<EmployeesEntity>> getBestEmployeesBySalesCount() {
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        return ResponseEntity.ok(employeesRepository.findBestEmployeesBySalesCount(oneYearAgo));
    }

    @GetMapping("/best/sales-amount")
    @Operation(summary = "Лучшие сотрудники по сумме продаж")
    public ResponseEntity<List<EmployeesEntity>> getBestEmployeesBySalesAmount() {
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        return ResponseEntity.ok(employeesRepository.findBestEmployeesBySalesAmount(oneYearAgo));
    }

    @GetMapping("/best/junior/smartwatches")
    @Operation(summary = "Лучший младший продавец-консультант по продаже умных часов")
    public ResponseEntity<EmployeesEntity> getBestJuniorConsultantBySmartWatches() {
        List<EmployeesEntity> bestConsultants = employeesRepository.findBestJuniorConsultantBySmartWatches();
        if (!bestConsultants.isEmpty()) {
            return ResponseEntity.ok(bestConsultants.get(0));
        }
        return ResponseEntity.notFound().build();
    }
}
