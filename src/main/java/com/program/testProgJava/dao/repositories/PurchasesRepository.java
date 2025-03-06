package com.program.testProgJava.dao.repositories;

import com.program.testProgJava.dao.entities.PurchasesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.sql.Date;

@Repository
public interface PurchasesRepository extends JpaRepository<PurchasesEntity, Long> {

    // Сумма проданных товаров за последний год
    @Query("SELECT SUM(ep.price * l.quantity) FROM PurchasesEntity p " +
            "JOIN ElectronicsProductsEntity ep ON p.productId = ep.productId " +
            "JOIN ElectronicsProductStoreLinkEntity l ON p.productId = l.productId " +
            "WHERE p.purchaseDate >= ?1 ")
    Double findTotalSales(Date startDate);

    // Сумма проданных товаров за наличные
    @Query("SELECT SUM(ep.price * l.quantity) FROM PurchasesEntity p " +
            "JOIN ElectronicsProductsEntity ep ON p.productId = ep.productId " +
            "JOIN ElectronicsProductStoreLinkEntity l ON p.productId = l.productId " +
            "WHERE p.purchaseDate >= ?1 " +
            "AND p.purchaseTypeId = ?2 ")
    Double findTotalCashSales(Date startDate,Long cashTypeId);
}

