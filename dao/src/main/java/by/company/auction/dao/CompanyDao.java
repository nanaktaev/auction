package by.company.auction.dao;

import by.company.auction.model.Company;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompanyDao extends AbstractDao<Company> {

    private static final ConnectionProvider connectionProvider = ConnectionProvider.getInstance();
    private static CompanyDao companyDaoInstance;

    private CompanyDao() {
    }

    @Override
    Class<Company> getEntityClass() {
        return Company.class;
    }

    @Override
    public Company create(Company company) {

        Integer companyId = null;

        try (PreparedStatement preparedStatement = connectionProvider.getConnection().prepareStatement(
                "INSERT INTO companies (name) VALUES (?) RETURNING id")) {
            preparedStatement.setString(1, company.getName());
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                companyId = resultSet.getInt(1);
            }
            resultSet.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return findById(companyId);
    }

    @Override
    public Company update(Company company) {

        try (PreparedStatement preparedStatement = connectionProvider.getConnection().prepareStatement(
                "UPDATE companies SET name = ? WHERE (id = ?)")) {
            preparedStatement.setString(1, company.getName());
            preparedStatement.setInt(2, company.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return company;
    }

    public Company findCompanyByName(String name) {

        try (PreparedStatement preparedStatement = connectionProvider.getConnection().prepareStatement(
                "SELECT * FROM companies WHERE name = ?")) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Company.class.getDeclaredConstructor().newInstance().buildFromResultSet(resultSet);
            }
            resultSet.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        throw new IllegalStateException("Не удалось получить данные.");
    }

    public static CompanyDao getInstance() {
        if (companyDaoInstance != null) {
            return companyDaoInstance;
        }
        companyDaoInstance = new CompanyDao();
        return companyDaoInstance;
    }
}
