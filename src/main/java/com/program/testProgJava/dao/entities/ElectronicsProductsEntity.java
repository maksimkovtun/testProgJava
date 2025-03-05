package com.program.testProgJava.dao.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "electronics_products", schema = "public", catalog = "testProgJava")
public class ElectronicsProductsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "product_id")
    private Long productId;

    public ElectronicsProductsEntity(String productId, String name, String electronicsTypeId, String price, String quantity, String isArchived, String description) {
        this.productId = Long.valueOf(productId);
        this.name = name;
        this.electronicsTypeId = Long.valueOf(electronicsTypeId);
        this.price = BigDecimal.valueOf(Long.parseLong(price));
        this.quantity = Integer.valueOf(quantity);
        this.isArchived = Boolean.valueOf(isArchived);
        this.description = description;
    }

    public ElectronicsProductsEntity() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Basic
    @Column(name = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "electronics_type_id")
    private Long electronicsTypeId;

    public Long getElectronicsTypeId() {
        return electronicsTypeId;
    }

    public void setElectronicsTypeId(Long electronicsTypeId) {
        this.electronicsTypeId = electronicsTypeId;
    }

    @Basic
    @Column(name = "price")
    private BigDecimal price;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    @Basic
    @Column(name = "is_archived")
    private Boolean isArchived;

    public Boolean getArchived() {
        return isArchived;
    }

    public void setArchived(Boolean archived) {
        isArchived = archived;
    }

    @Basic
    @Column(name = "description")
    private String description;

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
