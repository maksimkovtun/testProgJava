package com.program.testProgJava.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ElectronicsProductStoreLinkEntityPK implements Serializable {
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "store_id")
    private Long storeId;

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
