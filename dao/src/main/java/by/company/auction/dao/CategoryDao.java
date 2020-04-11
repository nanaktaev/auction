package by.company.auction.dao;

import by.company.auction.model.Category;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryDao extends AbstractDao<Category> {

    private static final ConnectionProvider connectionProvider = ConnectionProvider.getInstance();
    private static CategoryDao categoryDaoInstance;

    private CategoryDao() {
    }

    @Override
    protected Class<Category> getEntityClass() {
        return Category.class;
    }

    @Override
    public Category create(Category category) {

        Integer categoryId = null;

        try (PreparedStatement preparedStatement = connectionProvider.getConnection().prepareStatement(
                "INSERT INTO categories (name) VALUES (?) RETURNING id")) {
            preparedStatement.setString(1, category.getName());
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                categoryId = resultSet.getInt(1);
            }
            resultSet.close();

        } catch (SQLException e) {
            throw new IllegalStateException();
        }
        return findById(categoryId);
    }

    @Override
    public Category update(Category category) {

        try (PreparedStatement preparedStatement = connectionProvider.getConnection().prepareStatement(
                "UPDATE categories SET name = ? WHERE (id = ?)")) {
            preparedStatement.setString(1, category.getName());
            preparedStatement.setInt(2, category.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException();
        }
        return category;
    }

    public Category findCategoryByName(String name) {

        Category category = null;

        try (PreparedStatement preparedStatement = connectionProvider.getConnection().prepareStatement(
                "SELECT * FROM categories WHERE name = ?")) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                category = Category.class.getDeclaredConstructor().newInstance().buildFromResultSet(resultSet);
            }
            resultSet.close();

        } catch (Exception e) {
            throw new IllegalStateException();
        }
        return category;
    }

    public static CategoryDao getInstance() {
        if (categoryDaoInstance != null) {
            return categoryDaoInstance;
        }
        categoryDaoInstance = new CategoryDao();
        return categoryDaoInstance;
    }

}
