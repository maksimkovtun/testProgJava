package com.program.testProgJava.dao.repositories;

import com.program.testProgJava.dao.entities.EmployeeElectronicsTypeLinkEntity;
import com.program.testProgJava.dao.entities.EmployeeElectronicsTypeLinkEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeElectronicsTypeLinkRepository extends JpaRepository<EmployeeElectronicsTypeLinkEntity, EmployeeElectronicsTypeLinkEntityPK> {
}

