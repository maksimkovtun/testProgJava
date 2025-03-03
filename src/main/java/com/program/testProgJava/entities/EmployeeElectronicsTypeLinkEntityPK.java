package com.program.testProgJava.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EmployeeElectronicsTypeLinkEntityPK implements Serializable {
    @Column(name = "electronics_type_id")
    private Long electronicsTypeId;

    @Column(name = "employee_id")
    private Long employeeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeElectronicsTypeLinkEntityPK that = (EmployeeElectronicsTypeLinkEntityPK) o;
        return Objects.equals(electronicsTypeId, that.electronicsTypeId) &&
                Objects.equals(employeeId, that.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(electronicsTypeId, employeeId);
    }
}
