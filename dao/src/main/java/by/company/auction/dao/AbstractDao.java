package by.company.auction.dao;

import by.company.auction.annotaitions.TableName;
import by.company.auction.common.exceptions.DataAccessException;
import by.company.auction.model.BaseEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<T extends BaseEntity> {

    private final ConnectionProvider CONNECTION_PROVIDER = ConnectionProvider.getInstance();
    private final Class<T> T_CLASS = getEntityClass();
    private final String TABLE_NAME = T_CLASS.getAnnotation(TableName.class).value();
    private final Logger LOGGER = LogManager.getLogger(AbstractDao.class);

    protected abstract Class<T> getEntityClass();

    @SuppressWarnings("WeakerAccess")
    public T findById(Integer id) {

        T entity = null;

        try (PreparedStatement preparedStatement = CONNECTION_PROVIDER.getConnection().prepareStatement(
                "SELECT * FROM " + TABLE_NAME + " WHERE id = ?")) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                entity = ((T) T_CLASS.getDeclaredConstructor().newInstance().buildFromResultSet(resultSet));
            }
            resultSet.close();

        } catch (Exception e) {
            LOGGER.error(e);
            throw new DataAccessException();
        }
        return entity;
    }

    public void delete(Integer id) {

        try (PreparedStatement preparedStatement = CONNECTION_PROVIDER.getConnection().prepareStatement(
                "DELETE FROM " + TABLE_NAME + " WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error(e);
            throw new DataAccessException();
        }
    }

    public List<T> findAll() {

        List<T> entities = new ArrayList<>();

        try (PreparedStatement preparedStatement = CONNECTION_PROVIDER.getConnection().prepareStatement(
                "SELECT * FROM " + TABLE_NAME)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                entities.add((T) T_CLASS.getDeclaredConstructor().newInstance().buildFromResultSet(resultSet));
            }
            resultSet.close();

        } catch (Exception e) {
            LOGGER.error(e);
            throw new DataAccessException();
        }
        return entities;
    }

    public abstract T create(T entity);

    public abstract T update(T entity);

}
