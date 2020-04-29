package by.company.auction.dao;

import by.company.auction.common.exceptions.DataAccessException;
import by.company.auction.model.Category;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryDao extends AbstractDao<Category> {

    private final ConnectionProvider CONNECTION_PROVIDER = ConnectionProvider.getInstance();
    private final Logger LOGGER = LogManager.getLogger(CategoryDao.class);

    @Override
    protected Class<Category> getEntityClass() {
        return Category.class;
    }

    @Override
    public Category create(Category category) {

        Integer categoryId = null;

        try (PreparedStatement preparedStatement = CONNECTION_PROVIDER.getConnection().prepareStatement(
                "INSERT INTO categories (name) VALUES (?) RETURNING id")) {
            preparedStatement.setString(1, category.getName());
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                categoryId = resultSet.getInt(1);
            }
            resultSet.close();

        } catch (SQLException e) {
            LOGGER.error(e);
            throw new DataAccessException();
        }
        return findById(categoryId);
    }

    @Override
    public Category update(Category category) {

        try (PreparedStatement preparedStatement = CONNECTION_PROVIDER.getConnection().prepareStatement(
                "UPDATE categories SET name = ? WHERE (id = ?)")) {
            preparedStatement.setString(1, category.getName());
            preparedStatement.setInt(2, category.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error(e);
            throw new DataAccessException();
        }
        return category;
    }

    public Category findCategoryByName(String name) {

        Category category = null;

        try (PreparedStatement preparedStatement = CONNECTION_PROVIDER.getConnection().prepareStatement(
                "SELECT * FROM categories WHERE name = ?")) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                category = Category.class.getDeclaredConstructor().newInstance().buildFromResultSet(resultSet);
            }
            resultSet.close();

        } catch (Exception e) {
            LOGGER.error(e);
            throw new DataAccessException();
        }
        return category;
    }

    private static CategoryDao categoryDaoInstance;

    public static CategoryDao getInstance() {
        if (categoryDaoInstance != null) {
            return categoryDaoInstance;
        }
        categoryDaoInstance = new CategoryDao();
        return categoryDaoInstance;
    }

}
