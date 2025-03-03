package com.program.testProgJava.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "electronics_types", schema = "public", catalog = "testProgJava")
public class ElectronicsTypesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "electronics_type_id")
    private Long electronicsTypeId;
    @Basic
    @Column(name = "name")
    private String name;

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
        return Objects.equals(electronicsTypeId, that.electronicsTypeId) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(electronicsTypeId, name);
    }
}
