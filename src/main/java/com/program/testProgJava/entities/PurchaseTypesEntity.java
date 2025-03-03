package com.program.testProgJava.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "purchase_types", schema = "public", catalog = "testProgJava")
public class PurchaseTypesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "purchase_type_id")
    private Long purchaseTypeId;
    @Basic
    @Column(name = "name")
    private String name;

    public Long getPurchaseTypeId() {
        return purchaseTypeId;
    }

    public void setPurchaseTypeId(Long purchaseTypeId) {
        this.purchaseTypeId = purchaseTypeId;
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
        PurchaseTypesEntity that = (PurchaseTypesEntity) o;
        return Objects.equals(purchaseTypeId, that.purchaseTypeId) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(purchaseTypeId, name);
    }
}
