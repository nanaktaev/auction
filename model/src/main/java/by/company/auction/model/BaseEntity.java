package by.company.auction.model;

import java.sql.ResultSet;

public abstract class BaseEntity {
    private Integer id;

    public abstract BaseEntity buildFromResultSet(ResultSet resultSet);

    public BaseEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
