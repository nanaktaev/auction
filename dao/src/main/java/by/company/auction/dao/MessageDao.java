package by.company.auction.dao;

import by.company.auction.model.Message;
import by.company.auction.model.MessageType;

import java.util.ArrayList;
import java.util.List;

public class MessageDao extends AbstractDao<Message> {

    private static MessageDao messageDaoInstance;

    private MessageDao() {
        super(Message.class);
    }

    public List<Message> findMessagesByUserId(Integer userId) {
        ArrayList<Message> messages = new ArrayList<>();
        for (Message message : findAll()) {
            if (message.getUserId().equals(userId)) {
                messages.add(message);
            }
        }
        return messages;
    }

    public List<Message> findOutcomeMessagesByUserId(Integer userId) {
        ArrayList<Message> outcomeMessages = new ArrayList<>();
        for (Message message : findAll()) {
            if (message.getUserId().equals(userId) && message.getType().equals(MessageType.OUTCOME)) {
                outcomeMessages.add(message);
            }
        }
        return outcomeMessages;
    }

    public static MessageDao getInstance() {
        if (messageDaoInstance != null) {
            return messageDaoInstance;
        }
        messageDaoInstance = new MessageDao();
        return messageDaoInstance;
    }
}
