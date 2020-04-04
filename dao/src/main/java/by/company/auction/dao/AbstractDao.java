package by.company.auction.dao;

import by.company.auction.annotaitions.TableName;
import by.company.auction.model.BaseEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<T extends BaseEntity> {

    private static final ConnectionProvider connectionProvider = ConnectionProvider.getInstance();

    private final Class<T> tClass = getEntityClass();
    private final String tableName = tClass.getAnnotation(TableName.class).value();

    AbstractDao() {
    }

    abstract Class<T> getEntityClass();

    @SuppressWarnings("WeakerAccess")
    public T findById(Integer id) {

        T entity = null;

        try (PreparedStatement preparedStatement = connectionProvider.getConnection().prepareStatement(
                "SELECT * FROM " + tableName + " WHERE id = ?")) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                //noinspection unchecked
                entity = ((T) tClass.getDeclaredConstructor().newInstance().buildFromResultSet(resultSet));
            }
            resultSet.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return entity;
    }

    public void delete(Integer id) {

        try (PreparedStatement preparedStatement = connectionProvider.getConnection().prepareStatement(
                "DELETE FROM " + tableName + " WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @SuppressWarnings("WeakerAccess")
    public List<T> findAll() {

        List<T> entities = new ArrayList<>();

        try (PreparedStatement preparedStatement = connectionProvider.getConnection().prepareStatement(
                "SELECT * FROM " + tableName)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                //noinspection unchecked
                entities.add((T) tClass.getDeclaredConstructor().newInstance().buildFromResultSet(resultSet));
            }
            resultSet.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return entities;
    }

    public abstract T create(T entity);

    public abstract T update(T entity);

}
