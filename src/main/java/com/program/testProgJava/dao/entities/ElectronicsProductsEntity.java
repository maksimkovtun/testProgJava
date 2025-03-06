package com.program.testProgJava.dao.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "electronics_products", schema = "public", catalog = "testProgJava")
public class ElectronicsProductsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "electronics_type_id")
    private Long electronicsTypeId;

    @Basic
    @Column(name = "price")
    private BigDecimal price;

    @Basic
    @Column(name = "quantity")
    private Integer quantity;

    @Basic
    @Column(name = "is_archived")
    private Boolean isArchived;

    @Basic
    @Column(name = "description")
    private String description;

    public ElectronicsProductsEntity(String productId, String name, String electronicsTypeId, String price, String quantity, boolean isArchived, String description) {
        this.productId = Long.valueOf(productId);
        this.name = name;
        this.electronicsTypeId = Long.valueOf(electronicsTypeId);
        this.price = BigDecimal.valueOf(Long.parseLong(price));
        this.quantity = Integer.valueOf(quantity);
        this.isArchived = isArchived;
        this.description = description;
    }

    public ElectronicsProductsEntity() {
    }

    public Boolean getArchived() {
        return isArchived;
    }

    public void setArchived(Boolean archived) {
        isArchived = archived;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElectronicsProductsEntity that = (ElectronicsProductsEntity) o;
        return Objects.equals(productId, that.productId) && Objects.equals(name, that.name) && Objects.equals(electronicsTypeId, that.electronicsTypeId) && Objects.equals(price, that.price) && Objects.equals(quantity, that.quantity) && Objects.equals(isArchived, that.isArchived) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, name, electronicsTypeId, price, quantity, isArchived, description);
    }
}
