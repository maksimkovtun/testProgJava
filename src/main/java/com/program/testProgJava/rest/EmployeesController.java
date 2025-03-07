package com.program.testProgJava.rest;

import com.program.testProgJava.dao.entities.EmployeesEntity;
import com.program.testProgJava.dao.repositories.EmployeesRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@Tag(name = "Employee", description = "Сервис для управления сотрудниками магазина")
@RequestMapping("/store/api/employees")
public class EmployeesController {

    private final EmployeesRepository employeesRepository;

    public EmployeesController(EmployeesRepository employeesRepository) {
        this.employeesRepository = employeesRepository;
    }

    @GetMapping
    @Operation(summary = "Получить список сотрудников")
    public List<EmployeesEntity> getEmployees() {
        return employeesRepository.findAll();
    }

    @PostMapping("/add")
    @Operation(summary = "Добавить нового сотрудника")
    public ResponseEntity<EmployeesEntity> addEmployee(@RequestBody EmployeesEntity employee) {
        EmployeesEntity savedEmployee = employeesRepository.save(employee);
        return ResponseEntity.ok(savedEmployee);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Обновить сотрудника")
    public ResponseEntity<EmployeesEntity> updateEmployee(@PathVariable Long id, @RequestBody EmployeesEntity updatedEmployee) {
        return employeesRepository.findById(id)
                .map(existingEmployee -> {
                    existingEmployee.setLastName(updatedEmployee.getLastName());
                    existingEmployee.setFirstName(updatedEmployee.getFirstName());
                    existingEmployee.setMiddleName(updatedEmployee.getMiddleName());
                    existingEmployee.setPositionId(updatedEmployee.getPositionId());
                    existingEmployee.setStoreId(updatedEmployee.getStoreId());
                    existingEmployee.setBirthDate(updatedEmployee.getBirthDate());
                    existingEmployee.setGender(updatedEmployee.getGender());
                    employeesRepository.save(existingEmployee);
                    return ResponseEntity.ok(existingEmployee);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Удалить сотрудника")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        if (employeesRepository.existsById(id)) {
            employeesRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/sorted-employees")
    @Operation(summary = "Получить список сотрудников, начиная с лучших по продажам")
    public ResponseEntity<List<EmployeesEntity>> getSortedEmployees() {
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        List<EmployeesEntity> sortedEmployees = new ArrayList<>();
        List<EmployeesEntity> bestBySalesCount = employeesRepository.findBestEmployeesBySalesCount(Date.valueOf(oneYearAgo), PageRequest.of(0, 1));
        List<EmployeesEntity> bestBySalesAmount = employeesRepository.findBestEmployeesBySalesAmount(Date.valueOf(oneYearAgo), PageRequest.of(0, 1));
        List<EmployeesEntity> bestJunior = employeesRepository.findBestJuniorConsultantBySmartWatches(PageRequest.of(0, 1));
        if (!bestBySalesCount.isEmpty()) sortedEmployees.add(bestBySalesCount.get(0));
        if (!bestBySalesAmount.isEmpty()) sortedEmployees.add(bestBySalesAmount.get(0));
        if (!bestJunior.isEmpty()) sortedEmployees.add(bestJunior.get(0));
        List<EmployeesEntity> allEmployees = employeesRepository.findAll();
        allEmployees.removeIf(sortedEmployees::contains);
        sortedEmployees.addAll(allEmployees);
        return ResponseEntity.ok(sortedEmployees);
    }

    @GetMapping("/best/sales-count")
    @Operation(summary = "Лучший сотрудник по количеству продаж")
    public ResponseEntity<List<EmployeesEntity>> getBestEmployeesBySalesCount() {
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        return ResponseEntity.ok(employeesRepository.findBestEmployeesBySalesCount(Date.valueOf(oneYearAgo), PageRequest.of(0, 1)));
    }

    @GetMapping("/best/sales-amount")
    @Operation(summary = "Лучший сотрудник по сумме продаж")
    public ResponseEntity<List<EmployeesEntity>> getBestEmployeesBySalesAmount() {
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        return ResponseEntity.ok(employeesRepository.findBestEmployeesBySalesAmount(Date.valueOf(oneYearAgo), PageRequest.of(0, 1)));
    }

    @GetMapping("/best/junior/smartwatches")
    @Operation(summary = "Лучший младший продавец-консультант по продаже умных часов")
    public ResponseEntity<EmployeesEntity> getBestJuniorConsultantBySmartWatches() {
        List<EmployeesEntity> bestConsultants = employeesRepository.findBestJuniorConsultantBySmartWatches(PageRequest.of(0, 1));
        if (!bestConsultants.isEmpty()) {
            return ResponseEntity.ok(bestConsultants.get(0));
        }
        return ResponseEntity.notFound().build();
    }
}
