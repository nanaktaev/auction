package by.company.auction.services;

import by.company.auction.common.exceptions.NotFoundException;
import by.company.auction.dao.BidDao;
import by.company.auction.model.Bid;
import by.company.auction.model.Lot;
import by.company.auction.validators.BidValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.List;

import static by.company.auction.common.security.AuthenticationConfig.authentication;

public class BidService extends AbstractService<Bid, BidDao> {

    private static BidService bidServiceInstance;
    private final Logger LOGGER = LogManager.getLogger(BidService.class);

    public Bid findTopBidByLotId(Integer id) {

        LOGGER.debug("findTopBidByLotId() id = {}", id);
        return dao.findTopBidByLotId(id);

    }

    public Bid makeBid(Bid bid) {

        LOGGER.debug("makeBid() bid = {}", bid);

        Lot lot = LotService.getInstance().findById(bid.getLotId());
        Integer userId = authentication.getUserId();

        BidValidator.getInstance().validate(lot, bid, userId);

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

        LOGGER.debug("isUserLeading() lotId = {}, userId = {}", lotId, userId);
        return userId.equals(findTopBidByLotId(lotId).getUserId());

    }

    public List<Bid> findBidsByLotId(Integer lotId) {

        LOGGER.debug("findBidsByLotId() lotId = {}", lotId);

        if (!LotService.getInstance().exists(lotId)) {
            throw new NotFoundException("Ошибка. Лот по данному id не найден.");
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
