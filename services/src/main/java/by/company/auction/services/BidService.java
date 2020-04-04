package by.company.auction.services;

import by.company.auction.dao.BidDao;
import by.company.auction.model.Bid;
import by.company.auction.model.Lot;
import by.company.auction.validators.BidValidator;

import java.time.LocalDateTime;
import java.util.List;

import static by.company.auction.secuirty.AuthenticatonConfig.authentication;

public class BidService extends AbstractService<Bid, BidDao> {

    private static BidService bidServiceInstance;

    private BidService() {
    }

    public Bid findTopBidByLotId(Integer id) {
        return dao.findTopBidByLotId(id);
    }

    public Bid makeBid(Bid bid) {
        Lot lot = LotService.getInstance().findById(bid.getLotId());
        Integer userId = authentication.getUserId();

        BidValidator.validate(lot, bid, userId);

        Bid topBid = findTopBidByLotId(lot.getId());

        bid.setTime(LocalDateTime.now());
        bid.setUserId(userId);
        bid = create(bid);

        lot.setPrice(bid.getValue());
        if (lot.isBurning()) {
            lot.setCloses(LocalDateTime.now().plusMinutes(3));
        }
        LotService.getInstance().update(lot);

        if (topBid != null) {
            MessageService.getInstance().createWarningMessage(topBid.getUserId(), bid);
        }
        return bid;
    }

    boolean isUserLeading(Integer lotId, Integer userId) {
        return userId.equals(findTopBidByLotId(lotId).getUserId());
    }

    public List<Bid> findBidsByLotId(Integer lotId) {
        if (LotService.getInstance().findById(lotId) == null) {
            throw new IllegalStateException("Ошибка. Лот по данному id не найден.");
        }
        return dao.findBidsByLotId(lotId);
    }

    public static BidService getInstance() {
        if (bidServiceInstance != null) {
            return bidServiceInstance;
        }
        bidServiceInstance = new BidService();
        bidServiceInstance.setDao(BidDao.getInstance());
        return bidServiceInstance;
    }
}
