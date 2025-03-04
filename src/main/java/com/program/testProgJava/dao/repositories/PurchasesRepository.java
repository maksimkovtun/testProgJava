package com.program.testProgJava.dao.repositories;

import com.program.testProgJava.dao.entities.PurchasesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchasesRepository extends JpaRepository<PurchasesEntity, Long> {
}
