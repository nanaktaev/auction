package by.company.auction.services;

import by.company.auction.common.exceptions.NotYetPopulatedException;
import by.company.auction.converters.BidConverter;
import by.company.auction.dto.BidDto;
import by.company.auction.dto.LotDto;
import by.company.auction.dto.UserPrincipalAuction;
import by.company.auction.model.Bid;
import by.company.auction.repository.BidRepository;
import by.company.auction.security.SecurityValidator;
import by.company.auction.validators.BidValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
public class BidService extends AbstractService<Bid, BidDto, BidRepository, BidConverter> {

    @Autowired
    private SecurityValidator securityValidator;
    @Autowired
    private LotService lotService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private BidValidator bidValidator;

    protected BidService(BidRepository repository, BidConverter converter) {
        super(repository, converter);
    }

    public BidDto findTopBidByLotId(Integer id) {

        log.debug("findTopBidByLotId() id = {}", id);

        Bid topBid = repository.findTopBidByLotId(id);

        if (topBid == null) {
            return null;
        }

        return converter.convertToDto(topBid);
    }

    public BidDto makeBid(BidDto bidDto) {

        log.debug("makeBid() bidDto = {}", bidDto);

        LotDto lotDto = lotService.findById(bidDto.getLot().getId());

        UserPrincipalAuction principal = securityValidator.getUserPrincipal();

        Integer userId = principal.getId();

        bidValidator.validate(lotDto, bidDto, userId);

        BidDto topBid = findTopBidByLotId(lotDto.getId());

        bidDto.setTime(LocalDateTime.now());
        bidDto.setUser(userService.findById(userId));

        lotDto.setPrice(bidDto.getValue());
        if (bidValidator.isLotBurning(lotDto)) {
            lotDto.setCloses(LocalDateTime.now().plusMinutes(3));
        }
        lotService.update(lotDto);

        if (topBid != null) {
            messageService.createWarningMessage(topBid.getUser().getId(), bidDto);
        }

        return create(bidDto);
    }

    boolean isUserLeading(Integer lotId, Integer userId) {

        log.debug("isUserLeading() lotId = {}, userId = {}", lotId, userId);

        return userId.equals(findTopBidByLotId(lotId).getUser().getId());

    }

    public List<BidDto> findBidsByLotId(Integer lotId) {

        log.debug("findBidsByLotId() lotId = {}", lotId);

        List<Bid> bids = repository.findBidsByLotId(lotId);

        if (bids.isEmpty()) {
            throw new NotYetPopulatedException("There are no bids on this lot yet.");
        }

        return converter.convertListToDto(bids);
    }
}
