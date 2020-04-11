package by.company.auction.model;

import by.company.auction.annotaitions.TableName;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@TableName("messages")
public class Message extends BaseEntity {

    private String text;
    private LocalDateTime time;
    private MessageType type;
    private Integer userId;
    private Integer lotId;

    @Override
    public Message buildFromResultSet(ResultSet resultSet) throws SQLException {

        Message message = new Message();
        message.setId(resultSet.getInt(1));
        message.setText(resultSet.getString(2));
        message.setTime(resultSet.getTimestamp(3).toLocalDateTime());
        message.setType(MessageType.valueOf(resultSet.getString(4)));
        message.setLotId(resultSet.getInt(5));
        message.setUserId(resultSet.getInt(6));

        return message;
    }

    @Override
    public String toString() {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + " - " + text;
    }

    public Integer getId() {
        return super.getId();
    }

    public void setId(Integer id) {
        super.setId(id);
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
