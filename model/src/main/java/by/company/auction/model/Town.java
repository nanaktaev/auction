package by.company.auction.model;

import by.company.auction.annotaitions.TableName;

import java.sql.ResultSet;
import java.sql.SQLException;

@TableName("Towns")
public class Town extends BaseEntity {

    private String name;

    @Override
    public Town buildFromResultSet(ResultSet resultSet) throws SQLException {

        Town town = new Town();
        town.setId(resultSet.getInt(1));
        town.setName(resultSet.getString(2));

        return town;
    }

    @Override
    public String toString() {
        return super.getId() + ". " + name;
    }

    public Integer getId() {
        return super.getId();
    }

    public void setId(Integer id) {
        super.setId(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
