package com.program.testProgJava.dao.repositories;

import com.program.testProgJava.dao.entities.EmployeesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.sql.Date;
import java.util.List;

@Repository
public interface EmployeesRepository extends JpaRepository<EmployeesEntity, Long> {

    // Лучшие сотрудники по количеству продаж за год
    @Query("SELECT e FROM EmployeesEntity e " +
            "JOIN PurchasesEntity p ON e.employeeId = p.employeeId " +
            "WHERE p.purchaseDate >= ?1 " +
            "GROUP BY e " +
            "ORDER BY COUNT(p.purchaseId) DESC")
    List<EmployeesEntity> findBestEmployeesBySalesCount(Date oneYearAgo, Pageable pageable);

    // Лучшие сотрудники по сумме продаж за год
    @Query("SELECT e FROM EmployeesEntity e " +
            "JOIN PurchasesEntity p ON e.employeeId = p.employeeId " +
            "JOIN ElectronicsProductsEntity ep ON ep.productId = p.productId " +
            "WHERE p.purchaseDate >= ?1 " +
            "GROUP BY e " +
            "ORDER BY SUM(ep.price) DESC")
    List<EmployeesEntity> findBestEmployeesBySalesAmount(Date oneYearAgo, Pageable pageable);

    // Лучший младший продавец-консультант по продаже умных часов
    @Query("SELECT e FROM EmployeesEntity e " +
            "JOIN PurchasesEntity p ON e.employeeId = p.employeeId " +
            "JOIN ElectronicsProductsEntity ep ON ep.productId = p.productId " +
            "WHERE e.positionId = (SELECT pos.positionId FROM PositionsEntity pos WHERE pos.name = 'Младший продавец-консультант') " +
            "AND ep.name = 'Умные часы' " +
            "GROUP BY e " +
            "ORDER BY COUNT(p.purchaseId) DESC")
    List<EmployeesEntity> findBestJuniorConsultantBySmartWatches(Pageable pageable);
}
