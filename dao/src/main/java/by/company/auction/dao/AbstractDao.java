package by.company.auction.dao;

import by.company.auction.annotaitions.TableName;
import by.company.auction.model.BaseEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<T extends BaseEntity> {

    private static final ConnectionProvider connectionProvider = ConnectionProvider.getInstance();

    private final Class<T> tClass;

    AbstractDao(Class<T> tClass) {
        this.tClass = tClass;
    }

    public T create(T entity) {
//        entity.setId(generateNewId());
//        entitiesMap.put(entity.getId(), entity);
//
//        FileAccessor.saveEntitiesMap(entitiesMap, tClass);
        return entity;
    }

    @SuppressWarnings("WeakerAccess")
    public T findById(Integer id) {

        T entity = null;
        String tableName = tClass.getAnnotation(TableName.class).value();

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("select * from ? where id = ?")) {

            preparedStatement.setString(1, tableName);
            preparedStatement.setInt(2, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    entity = ((T) tClass.getDeclaredConstructor().newInstance().buildFromResultSet(resultSet));
                }

            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }

        return entity;
    }

    public T update(T entity) {
//        if (entity.getId() == null) {
//            throw new IllegalStateException("Объект по данному id не найден.");
//        }
//        entitiesMap.put(entity.getId(), entity);
//
//        FileAccessor.saveEntitiesMap(entitiesMap, tClass);
        return entity;
    }

    public List<T> findByIds(List<Integer> ids) {
        List<T> entities = new ArrayList<>();
        for (Integer id : ids) {
            entities.add(findById(id));
        }
        return entities;
    }

    public void delete(Integer id) {
//        entitiesMap.remove(id);
//        FileAccessor.saveEntitiesMap(entitiesMap, tClass);
    }

    @SuppressWarnings("WeakerAccess")
    public List<T> findAll() {

        List<T> entities = new ArrayList<>();
        String tableName = tClass.getAnnotation(TableName.class).value();

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("select * from ?")) {

            preparedStatement.setString(1, tableName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    entities.add((T) tClass.getDeclaredConstructor().newInstance().buildFromResultSet(resultSet));
                }

            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }

        return entities;
    }

}
