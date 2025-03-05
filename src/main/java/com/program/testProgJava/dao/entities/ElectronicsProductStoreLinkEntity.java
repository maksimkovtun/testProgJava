package com.program.testProgJava.dao.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "electronics_product_store_link", schema = "public", catalog = "testProgJava")
@IdClass(ElectronicsProductStoreLinkEntityPK.class)
public class ElectronicsProductStoreLinkEntity {
    @Id
    @Column(name = "product_id")
    private Long productId;

    public ElectronicsProductStoreLinkEntity(String storeId, String productId, String quantity) {
        this.storeId = Long.valueOf(storeId);
        this.productId = Long.valueOf(productId);
        this.quantity = Integer.valueOf(quantity);
    }

    public ElectronicsProductStoreLinkEntity() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Id
    @Column(name = "store_id")
    private Long storeId;

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    @Basic
    @Column(name = "quantity")
    private Integer quantity;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElectronicsProductStoreLinkEntity that = (ElectronicsProductStoreLinkEntity) o;
        return Objects.equals(productId, that.productId) && Objects.equals(storeId, that.storeId) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, storeId, quantity);
    }
}
