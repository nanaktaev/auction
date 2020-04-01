package by.company.auction.model;

import by.company.auction.annotaitions.TableName;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@TableName("Messages")
public class Message extends BaseEntity {
    private String text;
    private LocalDateTime time;
    private MessageType type;

    private Integer userId;
    private Integer lotId;

    public Message(Integer id, String text, LocalDateTime time, MessageType type, Integer userId, Integer lotId) {
        this.setId(id);
        this.text = text;
        this.time = time;
        this.type = type;
        this.userId = userId;
        this.lotId = lotId;
    }

    @Override
    public Message buildFromResultSet(ResultSet resultSet) {
        try {
            int id = resultSet.getInt(1);
            String text = resultSet.getString(2);
            LocalDateTime time = resultSet.getTimestamp(3).toLocalDateTime();
            MessageType type = MessageType.valueOf(resultSet.getString(4));
            int lotId = resultSet.getInt(5);
            int userId = resultSet.getInt(6);

            return new Message(id, text, time, type, userId, lotId);

        } catch (
                SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return null;
    }

    @Override
    public String toString() {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + " - " + text;
    }

    public Message() {
    }

    public Message(String text, LocalDateTime time, MessageType type, Integer userId, Integer lotId) {
        this.text = text;
        this.time = time;
        this.type = type;
        this.userId = userId;
        this.lotId = lotId;
    }

    public Message(LocalDateTime time, MessageType type, Integer userId, Integer lotId) {
        this.time = time;
        this.type = type;
        this.userId = userId;
        this.lotId = lotId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getLotId() {
        return lotId;
    }

    public void setLotId(Integer lotId) {
        this.lotId = lotId;
    }
}
