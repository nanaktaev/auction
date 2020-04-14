package by.company.auction.dao;

import by.company.auction.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao extends AbstractDao<User> {

    private static final ConnectionProvider connectionProvider = ConnectionProvider.getInstance();
    private static UserDao userDaoInstance;
    private static final Logger logger = LogManager.getLogger(UserDao.class);

    private UserDao() {
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    public User create(User user) {

        Integer userId = null;

        try (PreparedStatement preparedStatement = connectionProvider.getConnection().prepareStatement(
                "INSERT INTO users (email, password, username, role, company_id) VALUES (?, ?, ?, ?, ?) RETURNING id")) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getRole().name());
            preparedStatement.setObject(5, user.getCompanyId());
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                userId = resultSet.getInt(1);
            }
            resultSet.close();

        } catch (SQLException e) {
            logger.error("Failed to create a user.", e);
            throw new IllegalStateException();
        }
        return findById(userId);
    }

    @Override
    public User update(User user) {

        try (PreparedStatement preparedStatement = connectionProvider.getConnection().prepareStatement(
                "UPDATE users SET email = ?, password = ?, username = ?, role = ?, company_id = ? WHERE (id = ?)")) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getRole().name());
            preparedStatement.setObject(5, user.getCompanyId());
            preparedStatement.setInt(6, user.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.error("Failed to update a user.", e);
            throw new IllegalStateException();
        }
        return user;
    }

    public User findUserByEmail(String email) {

        User user = null;

        try (PreparedStatement preparedStatement = connectionProvider.getConnection().prepareStatement(
                "SELECT * FROM users WHERE email = ?")) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = User.class.getDeclaredConstructor().newInstance().buildFromResultSet(resultSet);
            }
            resultSet.close();

        } catch (Exception e) {
            logger.error("Failed to find a user by Email.", e);
            throw new IllegalStateException();
        }
        return user;
    }

    public User findUserByUsername(String username) {

        User user = null;

        try (PreparedStatement preparedStatement = connectionProvider.getConnection().prepareStatement(
                "SELECT * FROM users WHERE username = ?")) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = User.class.getDeclaredConstructor().newInstance().buildFromResultSet(resultSet);
            }
            resultSet.close();

        } catch (Exception e) {
            logger.error("Failed to find a user by username.", e);
            System.out.println(e.getMessage());
        }
        return user;
    }

    public static UserDao getInstance() {
        if (userDaoInstance != null) {
            return userDaoInstance;
        }
        userDaoInstance = new UserDao();
        return userDaoInstance;
    }

}
