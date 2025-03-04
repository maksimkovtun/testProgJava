package com.program.testProgJava.dao.repositories;

import com.program.testProgJava.dao.entities.ElectronicsTypesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectronicsTypesRepository extends JpaRepository<ElectronicsTypesEntity, Long> {
}