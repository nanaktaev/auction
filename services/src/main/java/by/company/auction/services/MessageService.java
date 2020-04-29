package by.company.auction.services;

import by.company.auction.dao.MessageDao;
import by.company.auction.model.Bid;

import by.company.auction.model.Lot;
import by.company.auction.model.Message;
import by.company.auction.model.MessageType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageService extends AbstractService<Message, MessageDao> {

    private static MessageService messageServiceInstance;
    private final Logger LOGGER = LogManager.getLogger(MessageService.class);

    @SuppressWarnings("WeakerAccess")
    public List<Message> findOutcomeMessagesByUserId(Integer userId) {

        LOGGER.debug("findOutcomeMessagesByUserId() userId = {}", userId);
        return dao.findOutcomeMessagesByUserId(userId);

    }

    public List<Message> findMessagesByUserId(Integer userId) {

        LOGGER.debug("findMessagesByUserId() userId = {}", userId);
        return dao.findMessagesByUserId(userId);

    }

    @SuppressWarnings("WeakerAccess")
    public void createOutcomeMessage(LocalDateTime time, Integer userId, Integer lotId, boolean userLeading) {

        LOGGER.debug("createOutcomeMessage() time = {}, userId = {}, lotId = {}, userLeading = {}",
                time, userId, lotId, userLeading);

        Message message = new Message();
        message.setTime(time);
        message.setType(MessageType.OUTCOME);
        message.setUserId(userId);
        message.setLotId(lotId);

        if (userLeading) {
            message.setText("Вы выиграли торги по лоту №" + lotId + "!");
        } else {
            message.setText("Вы проиграли торги по лоту №" + lotId + ".");
        }

        create(message);
    }

    void createWarningMessage(Integer userId, Bid bid) {

        LOGGER.debug("createWarningMessage() userId = {}, bid = {}", userId, bid);

        Message message = new Message();
        message.setTime(bid.getTime());
        message.setType(MessageType.WARNING);
        message.setUserId(userId);
        message.setLotId(bid.getLotId());
        message.setText("Ваша ставка по лоту №" + bid.getLotId() + " была перебита!");

        create(message);
    }

    public void prepareUserMessages(Integer userId) {

        LOGGER.debug("prepareUserMessages() userId = {}", userId);

        List<Message> userOutcomeMessages = findOutcomeMessagesByUserId(userId);
        List<Lot> expiredUserLots = LotService.getInstance().findExpiredLotsByUserId(userId);
        List<Integer> checkedLotIds = new ArrayList<>();

        userOutcomeMessages.forEach(message -> checkedLotIds.add(message.getLotId()));

        for (Integer checkedLotId : checkedLotIds) {
            expiredUserLots.removeIf(obj -> obj.getId().equals(checkedLotId));
        }

        if (!expiredUserLots.isEmpty()) {
            for (Lot lot : expiredUserLots) {
                if (BidService.getInstance().isUserLeading(lot.getId(), userId)) {
                    createOutcomeMessage(lot.getCloses(), userId, lot.getId(), true);
                } else {
                    createOutcomeMessage(lot.getCloses(), userId, lot.getId(), false);
                }
            }
        }
    }

    public static MessageService getInstance() {
        if (messageServiceInstance != null) {
            return messageServiceInstance;
        }
        messageServiceInstance = new MessageService();
        messageServiceInstance.setDao(MessageDao.getInstance());
        return messageServiceInstance;
    }
}
