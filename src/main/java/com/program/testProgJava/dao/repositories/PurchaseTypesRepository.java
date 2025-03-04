package com.program.testProgJava.dao.repositories;

import com.program.testProgJava.dao.entities.PurchaseTypesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseTypesRepository extends JpaRepository<PurchaseTypesEntity, Long> {
}
