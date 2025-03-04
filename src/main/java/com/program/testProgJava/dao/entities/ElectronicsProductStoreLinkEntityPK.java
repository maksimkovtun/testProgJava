package com.program.testProgJava.dao.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ElectronicsProductStoreLinkEntityPK implements Serializable {
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "store_id")
    private Long storeId;

    public ElectronicsProductStoreLinkEntityPK() {}

    public ElectronicsProductStoreLinkEntityPK(Long productId, Long storeId) {
        this.productId = productId;
        this.storeId = storeId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElectronicsProductStoreLinkEntityPK that = (ElectronicsProductStoreLinkEntityPK) o;
        return Objects.equals(productId, that.productId) && Objects.equals(storeId, that.storeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, storeId);
    }
}

