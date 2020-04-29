package by.company.auction.dao;

import by.company.auction.model.Town;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TownDao extends AbstractDao<Town> {

    private static final ConnectionProvider connectionProvider = ConnectionProvider.getInstance();
    private static TownDao townDaoInstance;

    private TownDao() {
    }

    @Override
    protected Class<Town> getEntityClass() {
        return Town.class;
    }

    @Override
    public Town create(Town town) {

        Integer townId = null;

        try (PreparedStatement preparedStatement = connectionProvider.getConnection().prepareStatement(
                "INSERT INTO towns (name) VALUES (?) RETURNING id")) {
            preparedStatement.setString(1, town.getName());
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                townId = resultSet.getInt(1);
            }
            resultSet.close();

        } catch (SQLException e) {
            throw new IllegalStateException();
        }
        return findById(townId);
    }

    @Override
    public Town update(Town town) {

        try (PreparedStatement preparedStatement = connectionProvider.getConnection().prepareStatement(
                "UPDATE towns SET name = ? WHERE (id = ?)")) {
            preparedStatement.setString(1, town.getName());
            preparedStatement.setInt(2, town.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException();
        }
        return town;
    }

    public Town findTownByName(String name) {

        Town town = null;

        try (PreparedStatement preparedStatement = connectionProvider.getConnection().prepareStatement(
                "SELECT * FROM towns WHERE name = ?")) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                town = Town.class.getDeclaredConstructor().newInstance().buildFromResultSet(resultSet);
            }
            resultSet.close();

        } catch (Exception e) {
            throw new IllegalStateException();
        }
        return town;
    }

    public static TownDao getInstance() {
        if (townDaoInstance != null) {
            return townDaoInstance;
        }
        townDaoInstance = new TownDao();
        return townDaoInstance;
    }

}
