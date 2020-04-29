package by.company.auction.services;

import by.company.auction.dao.MessageDao;
import by.company.auction.model.Bid;

import by.company.auction.model.Lot;
import by.company.auction.model.Message;
import by.company.auction.model.MessageType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageService extends AbstractService<Message, MessageDao> {

    private static MessageService messageServiceInstance;

    private MessageService() {
    }

    private List<Message> findOutcomeMessagesByUserId(Integer userId) {
        return dao.findOutcomeMessagesByUserId(userId);
    }

    public List<Message> findMessagesByUserId(Integer userId) {
        return dao.findMessagesByUserId(userId);
    }

    private void createOutcomeMessage(LocalDateTime time, Integer userId, Integer lotId, boolean userLeading) {

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

        Message message = new Message();
        message.setTime(bid.getTime());
        message.setType(MessageType.WARNING);
        message.setUserId(userId);
        message.setLotId(bid.getLotId());
        message.setText("Ваша ставка по лоту №" + bid.getLotId() + " была перебита!");

        create(message);
    }

    public void prepareUserMessages(Integer userId) {
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
