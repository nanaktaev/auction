package by.company.auction.dao;

import by.company.auction.common.exceptions.DataAccessException;
import by.company.auction.model.Company;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompanyDao extends AbstractDao<Company> {

    private final ConnectionProvider CONNECTION_PROVIDER = ConnectionProvider.getInstance();
    private final Logger LOGGER = LogManager.getLogger(CompanyDao.class);

    @Override
    protected Class<Company> getEntityClass() {
        return Company.class;
    }

    @Override
    public Company create(Company company) {

        Integer companyId = null;

        try (PreparedStatement preparedStatement = CONNECTION_PROVIDER.getConnection().prepareStatement(
                "INSERT INTO companies (name) VALUES (?) RETURNING id")) {
            preparedStatement.setString(1, company.getName());
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                companyId = resultSet.getInt(1);
            }
            resultSet.close();

        } catch (SQLException e) {
            LOGGER.error(e);
            throw new DataAccessException();
        }
        return findById(companyId);
    }

    @Override
    public Company update(Company company) {

        try (PreparedStatement preparedStatement = CONNECTION_PROVIDER.getConnection().prepareStatement(
                "UPDATE companies SET name = ? WHERE (id = ?)")) {
            preparedStatement.setString(1, company.getName());
            preparedStatement.setInt(2, company.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error(e);
            throw new DataAccessException();
        }
        return company;
    }

    public Company findCompanyByName(String name) {

        Company company = null;

        try (PreparedStatement preparedStatement = CONNECTION_PROVIDER.getConnection().prepareStatement(
                "SELECT * FROM companies WHERE name = ?")) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                company = Company.class.getDeclaredConstructor().newInstance().buildFromResultSet(resultSet);
            }
            resultSet.close();

        } catch (Exception e) {
            LOGGER.error(e);
            throw new DataAccessException();
        }
        return company;
    }

    private static CompanyDao companyDaoInstance;

    public static CompanyDao getInstance() {
        if (companyDaoInstance != null) {
            return companyDaoInstance;
        }
        companyDaoInstance = new CompanyDao();
        return companyDaoInstance;
    }
}
