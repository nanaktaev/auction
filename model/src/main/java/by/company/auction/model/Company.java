package by.company.auction.model;

import by.company.auction.annotaitions.TableName;

import java.sql.ResultSet;
import java.sql.SQLException;

@TableName("Companies")
public class Company extends BaseEntity {

    private String name;

    @Override
    public Company buildFromResultSet(ResultSet resultSet) throws SQLException {

        Company company = new Company();
        company.setId(resultSet.getInt(1));
        company.setName(resultSet.getString(2));

        return company;
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
