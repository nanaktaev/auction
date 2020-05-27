package by.company.auction.services;

import by.company.auction.common.exceptions.NotYetPopulatedException;
import by.company.auction.converters.MessageConverter;
import by.company.auction.dto.BidDto;
import by.company.auction.dto.LotDto;
import by.company.auction.dto.MessageDto;
import by.company.auction.model.Message;
import by.company.auction.model.MessageType;
import by.company.auction.repository.MessageRepository;
import by.company.auction.security.SecurityValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class MessageService extends AbstractService<Message, MessageDto, MessageRepository, MessageConverter> {

    @Autowired
    private SecurityValidator securityValidator;
    @Autowired
    private BidService bidService;
    @Autowired
    private LotService lotService;
    @Autowired
    private UserService userService;

    protected MessageService(MessageRepository repository, MessageConverter converter) {
        super(repository, converter);
    }

    List<MessageDto> findOutcomeMessagesByUserId(Integer userId) {

        log.debug("findOutcomeMessagesByUserId() userId = {}", userId);

        List<Message> messages = repository.findOutcomeMessagesByUserId(userId);

        return converter.convertListToDto(messages);
    }

    public List<MessageDto> findMessagesByUserId(Integer userId) {

        log.debug("findMessagesByUserId() userId = {}", userId);

        securityValidator.validateUserAccountOwnership(userId);

        prepareUserMessages(userId);

        List<Message> messages = repository.findMessagesByUserId(userId);

        if (messages.isEmpty()) {
            throw new NotYetPopulatedException("You don't have any messages yet.");
        }

        return converter.convertListToDto(messages);
    }

    void createOutcomeMessage(LocalDateTime time, Integer userId, Integer lotId, boolean userLeading) {

        log.debug("createOutcomeMessage() time = {}, userId = {}, lotId = {}, userLeading = {}",
                time, userId, lotId, userLeading);

        MessageDto messageDto = new MessageDto();
        messageDto.setTime(time);
        messageDto.setType(MessageType.OUTCOME);
        messageDto.setUser(userService.findById(userId));
        messageDto.setLot(lotService.findById(lotId));

        if (userLeading) {
            messageDto.setText("You've won bidding on a lot №" + lotId + "!");
        } else {
            messageDto.setText("You've lost bidding on a lot №" + lotId + ".");
        }

        create(messageDto);
    }

    void createWarningMessage(Integer userId, BidDto bidDto) {

        log.debug("createWarningMessage() userId = {}, bidDto = {}", userId, bidDto);

        MessageDto messageDto = new MessageDto();
        messageDto.setTime(bidDto.getTime());
        messageDto.setType(MessageType.WARNING);
        messageDto.setUser(userService.findById(userId));
        messageDto.setLot(bidDto.getLot());
        messageDto.setText("Your bid on a lot №" + bidDto.getLot().getId() + " has been outbid!");

        create(messageDto);
    }

    private void prepareUserMessages(Integer userId) {

        log.debug("prepareUserMessages() userId = {}", userId);

        List<MessageDto> userOutcomeMessages = findOutcomeMessagesByUserId(userId);
        List<LotDto> expiredUserLots = lotService.findExpiredLotsByUserId(userId);
        List<Integer> checkedLotIds = new ArrayList<>();

        userOutcomeMessages.forEach(message -> checkedLotIds.add(message.getLot().getId()));

        for (Integer checkedLotId : checkedLotIds) {
            expiredUserLots.removeIf(obj -> obj.getId().equals(checkedLotId));
        }

        if (!expiredUserLots.isEmpty()) {
            for (LotDto lot : expiredUserLots) {
                if (bidService.isUserLeading(lot.getId(), userId)) {
                    createOutcomeMessage(lot.getCloses(), userId, lot.getId(), true);
                } else {
                    createOutcomeMessage(lot.getCloses(), userId, lot.getId(), false);
                }
            }
        }
    }
}
