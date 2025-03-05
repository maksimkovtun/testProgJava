package com.program.testProgJava.dao.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "electronics_types", schema = "public", catalog = "testProgJava")
public class ElectronicsTypesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "electronics_type_id", nullable = false)
    private Long electronicsTypeId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    public ElectronicsTypesEntity(String electronicsTypeId, String name) {
        this.electronicsTypeId = Long.valueOf(electronicsTypeId);
        this.name = name;
    }

    public ElectronicsTypesEntity() {
    }

    public Long getElectronicsTypeId() {
        return electronicsTypeId;
    }

    public void setElectronicsTypeId(Long electronicsTypeId) {
        this.electronicsTypeId = electronicsTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElectronicsTypesEntity that = (ElectronicsTypesEntity) o;
        return Objects.equals(electronicsTypeId, that.electronicsTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(electronicsTypeId);
    }
}
