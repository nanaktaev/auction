package by.company.auction.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message extends Base{
    private String text;
    private LocalDateTime time;
    private MessageType type;

    private Integer userId;
    private Integer lotId;

    @Override
    public String toString() {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + " - " + text;
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
