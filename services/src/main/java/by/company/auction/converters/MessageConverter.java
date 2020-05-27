package by.company.auction.converters;

import by.company.auction.dto.MessageDto;
import by.company.auction.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageConverter extends AbstractConverter<Message, MessageDto> {

    @Autowired
    private UserConverter userConverter;
    @Autowired
    private LotConverter lotConverter;

    @Override
    public MessageDto convertToDto(Message message) {

        MessageDto messageDto = new MessageDto();
        messageDto.setId(message.getId());
        messageDto.setText(message.getText());
        messageDto.setTime(message.getTime());
        messageDto.setType(message.getType());
        if (message.getUser() != null) {
            messageDto.setUser(userConverter.convertToDto(message.getUser()));
        }
        if (message.getLot() != null) {
            messageDto.setLot(lotConverter.convertToDto(message.getLot()));
        }
        return messageDto;
    }

    @Override
    public Message convertToEntity(MessageDto messageDto) {

        Message message = new Message();
        message.setId(messageDto.getId());
        message.setText(messageDto.getText());
        message.setTime(messageDto.getTime());
        message.setType(messageDto.getType());
        if (messageDto.getUser() != null) {
            message.setUser(userConverter.convertToEntity(messageDto.getUser()));
        }
        if (messageDto.getLot() != null) {
            message.setLot(lotConverter.convertToEntity(messageDto.getLot()));
        }
        return message;
    }
}
