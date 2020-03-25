package by.company.auction.services;

import by.company.auction.dao.BidDao;
import by.company.auction.model.Bid;
import by.company.auction.model.Lot;
import by.company.auction.model.User;
import validators.BidValidator;

import java.time.LocalDateTime;
import java.util.List;

import static by.company.auction.secuirty.AuthenticatonContainer.authentication;

public class BidService extends AbstractService<Bid, BidDao> {

    private static BidService bidServiceInstance;

    private BidService() {
    }

    public Bid findTopBid(Lot lot) {
        if (!lot.getBidIds().isEmpty()) {
            Integer topBidId = 0;

            for (Integer bidId : lot.getBidIds()) {
                if (bidId > topBidId) topBidId = bidId;
            }
            return findById(topBidId);
        }
        return null;
    }

    public Bid makeBid(Bid bid) {
        LotService lotService = LotService.getInstance();
        UserService userService = UserService.getInstance();

        Lot lot = lotService.findById(bid.getLotId());
        Bid topBid = findTopBid(lot);
        Integer userId = authentication.getUserId();

        BidValidator.validate(lot, bid, userId);

        bid.setTime(LocalDateTime.now());
        bid.setUserId(userId);
        bid = create(bid);

        lot.setPrice(bid.getValue());
        lot.getBidIds().add(bid.getId());
        lot.getUserIds().add(userId);
        if (lot.isBurning()) lot.setCloses(LocalDateTime.now().plusMinutes(3));
        lotService.update(lot);

        User user = userService.findById(userId);
        user.getBidIds().add(bid.getId());
        if (!user.getLotIds().contains(lot.getId()))
            user.getLotIds().add(lot.getId());
        userService.update(user);

        if (topBid != null)
            MessageService.getInstance().createWarningMessage(topBid.getUserId(), bid);

        return bid;
    }

    public boolean isUserLeading(Lot lot, Integer userId) {
        List<Bid> bids = findByIds(lot.getBidIds());
        Bid topBid = bids.get(0);
        for (Bid bid : bids) {
            if (bid.getId() > topBid.getId()) topBid = bid;
        }
        return (userId.equals(topBid.getUserId()));
    }

    public List<Bid> findBidsByLotId(Integer lotId) {
        if (LotService.getInstance().findById(lotId) == null)
            throw new IllegalStateException("Ошибка. Лот по данному id не найден.");
        return findByIds(LotService.getInstance().findById(lotId).getBidIds());
    }

    void deleteBid(Integer bidId) {
        LotService lotService = LotService.getInstance();
        UserService userService = UserService.getInstance();

        Bid bid = findById(bidId);
        User user = userService.findById(bid.getUserId());
        Lot lot = lotService.findById(bid.getLotId());

        user.getBidIds().removeIf(bidId::equals);
        lot.getBidIds().removeIf(bidId::equals);

        lotService.update(lot);
        userService.update(user);
        dao.delete(bid.getId());
    }

    public static BidService getInstance() {
        if (bidServiceInstance != null) return bidServiceInstance;
        bidServiceInstance = new BidService();
        bidServiceInstance.setDao(BidDao.getInstance());
        return bidServiceInstance;
    }
}
