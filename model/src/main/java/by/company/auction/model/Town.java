package by.company.auction.model;

import by.company.auction.annotaitions.TableName;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@TableName("Towns")
public class Town extends BaseEntity {
    private String name;

    @Override
    public Town buildFromResultSet(ResultSet resultSet) {
        try {
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);

            return new Town(id, name);

        } catch (
                SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return null;
    }

    public Town(Integer id, String name) {
        this.setId(id);
        this.name = name;
    }

    @Override
    public String toString() {
        return super.toString() + ". " + name;
    }

    public Town() {
    }

    public Town(String name, Integer... lotIds) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
