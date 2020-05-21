package by.company.auction.services;

import by.company.auction.common.exceptions.NotYetPopulatedException;
import by.company.auction.model.Bid;
import by.company.auction.model.Lot;
import by.company.auction.model.Message;
import by.company.auction.model.MessageType;
import by.company.auction.repository.MessageRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@Transactional
public class MessageService extends AbstractService<Message, MessageRepository> {

    @Autowired
    private BidService bidService;
    @Autowired
    private LotService lotService;
    @Autowired
    private UserService userService;

    protected MessageService(MessageRepository repository) {
        super(repository);
    }


    @SuppressWarnings("WeakerAccess")
    public List<Message> findOutcomeMessagesByUserId(Integer userId) {

        log.debug("findOutcomeMessagesByUserId() userId = {}", userId);
        return repository.findOutcomeMessagesByUserId(userId);

    }

    public List<Message> findMessagesByUserId(Integer userId) {

        log.debug("findMessagesByUserId() userId = {}", userId);

        List<Message> messages = repository.findMessagesByUserId(userId);

        if (messages.isEmpty()) {
            throw new NotYetPopulatedException("У вас пока нет сообщений.");
        }

        return messages;
    }

    @SuppressWarnings("WeakerAccess")
    public void createOutcomeMessage(LocalDateTime time, Integer userId, Integer lotId, boolean userLeading) {

        log.debug("createOutcomeMessage() time = {}, userId = {}, lotId = {}, userLeading = {}",
                time, userId, lotId, userLeading);

        Message message = new Message();
        message.setTime(time);
        message.setType(MessageType.OUTCOME);
        message.setUser(userService.findById(userId));
        message.setLot(lotService.findById(lotId));

        if (userLeading) {
            message.setText("Вы выиграли торги по лоту №" + lotId + "!");
        } else {
            message.setText("Вы проиграли торги по лоту №" + lotId + ".");
        }

        create(message);
    }

    void createWarningMessage(Integer userId, Bid bid) {

        log.debug("createWarningMessage() userId = {}, bid = {}", userId, bid);

        Message message = new Message();
        message.setTime(bid.getTime());
        message.setType(MessageType.WARNING);
        message.setUser(userService.findById(userId));
        message.setLot(bid.getLot());
        message.setText("Ваша ставка по лоту №" + bid.getLot().getId() + " была перебита!");

        create(message);
    }

    public void prepareUserMessages(Integer userId) {

        log.debug("prepareUserMessages() userId = {}", userId);

        List<Message> userOutcomeMessages = findOutcomeMessagesByUserId(userId);
        List<Lot> expiredUserLots = lotService.findExpiredLotsByUserId(userId);
        List<Integer> checkedLotIds = new ArrayList<>();

        userOutcomeMessages.forEach(message -> checkedLotIds.add(message.getLot().getId()));

        for (Integer checkedLotId : checkedLotIds) {
            expiredUserLots.removeIf(obj -> obj.getId().equals(checkedLotId));
        }

        if (!expiredUserLots.isEmpty()) {
            for (Lot lot : expiredUserLots) {
                if (bidService.isUserLeading(lot.getId(), userId)) {
                    createOutcomeMessage(lot.getCloses(), userId, lot.getId(), true);
                } else {
                    createOutcomeMessage(lot.getCloses(), userId, lot.getId(), false);
                }
            }
        }
    }

}
