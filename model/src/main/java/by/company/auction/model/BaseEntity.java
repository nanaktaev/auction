package by.company.auction.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseEntity {

    private Integer id;

    public abstract <T extends BaseEntity> T buildFromResultSet(ResultSet resultSet) throws SQLException;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
