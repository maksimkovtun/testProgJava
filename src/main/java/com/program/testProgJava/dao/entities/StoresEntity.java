package com.program.testProgJava.dao.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "stores", schema = "public", catalog = "testProgJava")
public class StoresEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "store_id")
    private Long storeId;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "address")
    private String address;

    public StoresEntity(String storeId, String name, String address) {
        this.storeId = Long.valueOf(storeId);
        this.name = name;
        this.address = address;
    }

    public StoresEntity() {
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoresEntity that = (StoresEntity) o;
        return Objects.equals(storeId, that.storeId) && Objects.equals(name, that.name) && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, name, address);
    }
}
