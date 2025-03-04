package com.program.testProgJava.dao.repositories;

import com.program.testProgJava.dao.entities.ElectronicsProductsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectronicsProductsRepository extends JpaRepository<ElectronicsProductsEntity, Long> {
}
