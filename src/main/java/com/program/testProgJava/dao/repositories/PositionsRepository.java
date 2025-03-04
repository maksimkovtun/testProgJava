package com.program.testProgJava.dao.repositories;

import com.program.testProgJava.dao.entities.PositionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionsRepository extends JpaRepository<PositionsEntity, Long> {
}
