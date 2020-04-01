package by.company.auction.model;

import by.company.auction.annotaitions.TableName;

import java.sql.ResultSet;
import java.sql.SQLException;

@TableName("Companies")
public class Company extends BaseEntity {
    private String name;

    @Override
    public Company buildFromResultSet(ResultSet resultSet) {
        try {
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);

            return new Company(id, name);

        } catch (
                SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return null;
    }

    public Company(Integer id, String name) {
        this.setId(id);
        this.name = name;
    }

    @Override
    public String toString() {
        return super.toString() + ". " + name;
    }

    public Company() {
    }

    public Company(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
