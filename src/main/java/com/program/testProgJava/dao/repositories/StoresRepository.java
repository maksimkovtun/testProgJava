package com.program.testProgJava.dao.repositories;

import com.program.testProgJava.dao.entities.StoresEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoresRepository extends JpaRepository<StoresEntity, Long> {
}
