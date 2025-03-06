    package com.program.testProgJava.dao.entities;

    import javax.persistence.*;
    import java.sql.Date;
    import java.util.Objects;

    @Entity
    @Table(name = "purchases", schema = "public", catalog = "testProgJava")
    public class PurchasesEntity {
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        @Column(name = "purchase_id")
        private Long purchaseId;
        @Basic
        @Column(name = "product_id")
        private Long productId;
        @Basic
        @Column(name = "employee_id")
        private Long employeeId;
        @Basic
        @Column(name = "purchase_date")
        private Date purchaseDate;
        @Basic
        @Column(name = "purchase_type_id")
        private Long purchaseTypeId;
        @Basic
        @Column(name = "store_id")
        private Long storeId;

        public PurchasesEntity(String purchaseId, String productId, String employeeId, Date purchaseDate, String purchaseTypeId, String storeId) {
            this.purchaseId = Long.valueOf(purchaseId);
            this.productId = Long.valueOf(productId);
            this.employeeId = Long.valueOf(employeeId);
            this.purchaseDate = purchaseDate;
            this.purchaseTypeId = Long.valueOf(purchaseTypeId);
            this.storeId = Long.valueOf(storeId);
        }

        public PurchasesEntity() {
        }

        public Long getPurchaseId() {
            return purchaseId;
        }

        public void setPurchaseId(Long purchaseId) {
            this.purchaseId = purchaseId;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public Long getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(Long employeeId) {
            this.employeeId = employeeId;
        }

        public Long getStoreId() {
            return storeId;
        }

        public void setStoreId(Long storeId) {
            this.storeId = storeId;
        }

        public Date getPurchaseDate() {
            return purchaseDate;
        }

        public void setPurchaseDate(Date purchaseDate) {
            this.purchaseDate = purchaseDate;
        }

        public Long getPurchaseTypeId() {
            return purchaseTypeId;
        }

        public void setPurchaseTypeId(Long purchaseTypeId) {
            this.purchaseTypeId = purchaseTypeId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PurchasesEntity that = (PurchasesEntity) o;
            return Objects.equals(purchaseId, that.purchaseId) && Objects.equals(productId, that.productId) && Objects.equals(employeeId, that.employeeId) && Objects.equals(storeId, that.storeId) && Objects.equals(purchaseDate, that.purchaseDate) && Objects.equals(purchaseTypeId, that.purchaseTypeId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(purchaseId, productId, employeeId, storeId, purchaseDate, purchaseTypeId);
        }
    }
