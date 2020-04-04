package by.company.auction.model;

import by.company.auction.annotaitions.TableName;

import java.sql.ResultSet;
import java.sql.SQLException;

@TableName("Categories")
public class Category extends BaseEntity {

    private String name;

    @Override
    public Category buildFromResultSet(ResultSet resultSet) throws SQLException {

        Category category = new Category();
        category.setId(resultSet.getInt(1));
        category.setName(resultSet.getString(2));

        return category;
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
