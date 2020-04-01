package by.company.auction.model;

import by.company.auction.annotaitions.TableName;

import java.sql.ResultSet;
import java.sql.SQLException;

@TableName("Categories")
public class Category extends BaseEntity {
    private String name;

    @Override
    public Category buildFromResultSet(ResultSet resultSet) {
        try {
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);

            return new Category(id, name);

        } catch (
                SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return null;
    }

    @Override
    public String toString() {
        return super.toString() + ". " + name;
    }

    public Category() {
    }

    public Category(Integer id, String name) {
        this.setId(id);
        this.name = name;
    }

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
