package by.company.auction.services;

import by.company.auction.common.exceptions.NotYetPopulatedException;
import by.company.auction.model.Bid;
import by.company.auction.model.Lot;
import by.company.auction.repository.BidRepository;
import by.company.auction.validators.BidValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static by.company.auction.common.security.AuthenticationConfig.authentication;

@Log4j2
@Service
@Transactional
public class BidService extends AbstractService<Bid, BidRepository> {

    @Autowired
    private LotService lotService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private BidValidator bidValidator;

    protected BidService(BidRepository repository) {
        super(repository);
    }

    public Bid findTopBidByLotId(Integer id) {

        log.debug("findTopBidByLotId() id = {}", id);
        return repository.findTopBidByLotId(id);

    }

    public Bid makeBid(Bid bid) {

        log.debug("makeBid() bid = {}", bid);

        Lot lot = lotService.findById(bid.getLot().getId());
        Integer userId = authentication.getUserId();

        bidValidator.validate(lot, bid, userId);

        Bid topBid = findTopBidByLotId(lot.getId());

        bid.setTime(LocalDateTime.now());
        bid.setUser(userService.findById(userId));
        bid = create(bid);

        lot.setPrice(bid.getValue());
        if (lot.isBurning()) {
            lot.setCloses(LocalDateTime.now().plusMinutes(3));
        }
        lotService.update(lot);

        if (topBid != null) {
            messageService.createWarningMessage(topBid.getUser().getId(), bid);
        }
        return bid;
    }

    boolean isUserLeading(Integer lotId, Integer userId) {

        log.debug("isUserLeading() lotId = {}, userId = {}", lotId, userId);
        return userId.equals(findTopBidByLotId(lotId).getUser().getId());

    }

    public List<Bid> findBidsByLotId(Integer lotId) {

        log.debug("findBidsByLotId() lotId = {}", lotId);

        List<Bid> bids = repository.findBidsByLotId(lotId);

        if (bids.isEmpty()) {
            throw new NotYetPopulatedException("На этом лоте пока нет ставок.");
        }

        return bids;
    }

}
