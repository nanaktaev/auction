package by.company.auction.dao;

import by.company.auction.common.exceptions.DataAccessException;
import by.company.auction.model.Town;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TownDao extends AbstractDao<Town> {

    private final ConnectionProvider CONNECTION_PROVIDER = ConnectionProvider.getInstance();
    private final Logger LOGGER = LogManager.getLogger(TownDao.class);

    @Override
    protected Class<Town> getEntityClass() {
        return Town.class;
    }

    @Override
    public Town create(Town town) {

        Integer townId = null;

        try (PreparedStatement preparedStatement = CONNECTION_PROVIDER.getConnection().prepareStatement(
                "INSERT INTO towns (name) VALUES (?) RETURNING id")) {
            preparedStatement.setString(1, town.getName());
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                townId = resultSet.getInt(1);
            }
            resultSet.close();

        } catch (SQLException e) {
            LOGGER.error(e);
            throw new DataAccessException();
        }
        return findById(townId);
    }

    @Override
    public Town update(Town town) {

        try (PreparedStatement preparedStatement = CONNECTION_PROVIDER.getConnection().prepareStatement(
                "UPDATE towns SET name = ? WHERE (id = ?)")) {
            preparedStatement.setString(1, town.getName());
            preparedStatement.setInt(2, town.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error(e);
            throw new DataAccessException();
        }
        return town;
    }

    public Town findTownByName(String name) {

        Town town = null;

        try (PreparedStatement preparedStatement = CONNECTION_PROVIDER.getConnection().prepareStatement(
                "SELECT * FROM towns WHERE name = ?")) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                town = Town.class.getDeclaredConstructor().newInstance().buildFromResultSet(resultSet);
            }
            resultSet.close();

        } catch (Exception e) {
            LOGGER.error(e);
            throw new DataAccessException();
        }
        return town;
    }

    private static TownDao townDaoInstance;

    public static TownDao getInstance() {
        if (townDaoInstance != null) {
            return townDaoInstance;
        }
        townDaoInstance = new TownDao();
        return townDaoInstance;
    }

}
