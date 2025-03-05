package com.program.testProgJava.dao.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "positions", schema = "public", catalog = "testProgJava")
public class PositionsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "position_id")
    private Long positionId;
    @Basic
    @Column(name = "name")
    private String name;

    public PositionsEntity(String positionId, String name) {
        this.positionId = Long.valueOf(positionId);
        this.name = name;
    }

    public PositionsEntity() {
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
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
        PositionsEntity that = (PositionsEntity) o;
        return Objects.equals(positionId, that.positionId) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(positionId, name);
    }
}
