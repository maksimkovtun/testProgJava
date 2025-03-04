package com.program.testProgJava.dao.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "employee_electronics_type_link", schema = "public", catalog = "testProgJava")
@IdClass(EmployeeElectronicsTypeLinkEntityPK.class)
public class EmployeeElectronicsTypeLinkEntity {

    @Id
    @Column(name = "electronics_type_id", nullable = false)
    private Long electronicsTypeId;

    @Id
    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    public Long getElectronicsTypeId() {
        return electronicsTypeId;
    }

    public void setElectronicsTypeId(Long electronicsTypeId) {
        this.electronicsTypeId = electronicsTypeId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeElectronicsTypeLinkEntity that = (EmployeeElectronicsTypeLinkEntity) o;
        return Objects.equals(electronicsTypeId, that.electronicsTypeId) &&
                Objects.equals(employeeId, that.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(electronicsTypeId, employeeId);
    }
}
