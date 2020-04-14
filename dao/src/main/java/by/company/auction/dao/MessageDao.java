package by.company.auction.dao;

import by.company.auction.model.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDao extends AbstractDao<Message> {

    private static final ConnectionProvider connectionProvider = ConnectionProvider.getInstance();
    private static MessageDao messageDaoInstance;
    private static final Logger logger = LogManager.getLogger(UserDao.class);

    private MessageDao() {
    }

    @Override
    protected Class<Message> getEntityClass() {
        return Message.class;
    }

    @Override
    public Message create(Message message) {

        Integer messageId = null;

        try (PreparedStatement preparedStatement = connectionProvider.getConnection().prepareStatement(
                "INSERT INTO messages (text, time, type, lot_id, user_id) VALUES (?, ?, ?, ?, ?) RETURNING id")) {
            preparedStatement.setString(1, message.getText());
            preparedStatement.setTimestamp(2, java.sql.Timestamp.valueOf(message.getTime()));
            preparedStatement.setString(3, message.getType().name());
            preparedStatement.setInt(4, message.getLotId());
            preparedStatement.setInt(5, message.getUserId());
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                messageId = resultSet.getInt(1);
            }
            resultSet.close();

        } catch (SQLException e) {
            logger.error("Failed to create a message.", e);
            throw new IllegalStateException();
        }
        return findById(messageId);
    }

    @Override
    public Message update(Message message) {

        try (PreparedStatement preparedStatement = connectionProvider.getConnection().prepareStatement(
                "UPDATE messages SET text = ?, time = ?, type = ?, lot_id = ?, user_id = ? WHERE (id = ?)")) {
            preparedStatement.setString(1, message.getText());
            preparedStatement.setTimestamp(2, java.sql.Timestamp.valueOf(message.getTime()));
            preparedStatement.setString(3, message.getType().name());
            preparedStatement.setInt(4, message.getLotId());
            preparedStatement.setInt(5, message.getUserId());
            preparedStatement.setInt(6, message.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.error("Failed to update a message.", e);
            throw new IllegalStateException();
        }

        return message;
    }

    public List<Message> findMessagesByUserId(Integer userId) {

        List<Message> messages = new ArrayList<>();

        try (PreparedStatement preparedStatement = connectionProvider.getConnection().prepareStatement(
                "SELECT * FROM messages WHERE user_id = ?")) {
            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                messages.add(Message.class.getDeclaredConstructor().newInstance().buildFromResultSet(resultSet));
            }
            resultSet.close();

        } catch (Exception e) {
            logger.error("Failed to find messages of a user.", e);
            throw new IllegalStateException();
        }
        return messages;
    }

    public List<Message> findOutcomeMessagesByUserId(Integer userId) {

        List<Message> messages = new ArrayList<>();

        try (PreparedStatement preparedStatement = connectionProvider.getConnection().prepareStatement(
                "SELECT * FROM messages WHERE user_id = ? AND type = 'OUTCOME'")) {
            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                messages.add(Message.class.getDeclaredConstructor().newInstance().buildFromResultSet(resultSet));
            }
            resultSet.close();

        } catch (Exception e) {
            logger.error("Failed to find outcome messages of a user.", e);
            throw new IllegalStateException();
        }
        return messages;
    }

    public static MessageDao getInstance() {
        if (messageDaoInstance != null) {
            return messageDaoInstance;
        }
        messageDaoInstance = new MessageDao();
        return messageDaoInstance;
    }
}
