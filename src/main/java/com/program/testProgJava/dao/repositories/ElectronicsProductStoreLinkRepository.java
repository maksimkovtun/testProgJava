package com.program.testProgJava.dao.repositories;

import com.program.testProgJava.dao.entities.ElectronicsProductStoreLinkEntity;
import com.program.testProgJava.dao.entities.ElectronicsProductStoreLinkEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectronicsProductStoreLinkRepository extends JpaRepository<ElectronicsProductStoreLinkEntity, ElectronicsProductStoreLinkEntityPK> {
}