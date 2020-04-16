package by.company.auction.dao;

import by.company.auction.common.exceptions.DataAccessException;
import by.company.auction.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao extends AbstractDao<User> {

    private final ConnectionProvider CONNECTION_PROVIDER = ConnectionProvider.getInstance();
    private final Logger LOGGER = LogManager.getLogger(UserDao.class);

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    public User create(User user) {

        Integer userId = null;

        try (PreparedStatement preparedStatement = CONNECTION_PROVIDER.getConnection().prepareStatement(
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
            LOGGER.error(e);
            throw new DataAccessException();
        }
        return findById(userId);
    }

    @Override
    public User update(User user) {

        try (PreparedStatement preparedStatement = CONNECTION_PROVIDER.getConnection().prepareStatement(
                "UPDATE users SET email = ?, password = ?, username = ?, role = ?, company_id = ? WHERE (id = ?)")) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getRole().name());
            preparedStatement.setObject(5, user.getCompanyId());
            preparedStatement.setInt(6, user.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error(e);
            throw new DataAccessException();
        }
        return user;
    }

    public User findUserByEmail(String email) {

        User user = null;

        try (PreparedStatement preparedStatement = CONNECTION_PROVIDER.getConnection().prepareStatement(
                "SELECT * FROM users WHERE email = ?")) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = User.class.getDeclaredConstructor().newInstance().buildFromResultSet(resultSet);
            }
            resultSet.close();

        } catch (Exception e) {
            LOGGER.error(e);
            throw new DataAccessException();
        }
        return user;
    }

    public User findUserByUsername(String username) {

        User user = null;

        try (PreparedStatement preparedStatement = CONNECTION_PROVIDER.getConnection().prepareStatement(
                "SELECT * FROM users WHERE username = ?")) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = User.class.getDeclaredConstructor().newInstance().buildFromResultSet(resultSet);
            }
            resultSet.close();

        } catch (Exception e) {
            LOGGER.error(e);
            throw new DataAccessException();
        }
        return user;
    }

    private static UserDao userDaoInstance;

    public static UserDao getInstance() {
        if (userDaoInstance != null) {
            return userDaoInstance;
        }
        userDaoInstance = new UserDao();
        return userDaoInstance;
    }

}
