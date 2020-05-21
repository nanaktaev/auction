package by.company.auction.validators;

import by.company.auction.common.exceptions.BusinessException;
import by.company.auction.common.exceptions.NoSuchEntityException;
import by.company.auction.model.Bid;
import by.company.auction.model.Lot;
import by.company.auction.services.BidService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Log4j2
@Component
public class BidValidator {

    @Autowired
    private BidService bidService;

    public void validate(Lot lot, Bid bid, Integer userId) {

        log.debug("validate() lot = {}, bid = {}, userId = {}", lot, bid, userId);

        validateLotExistence(lot);
        validateLotClosingDate(lot);
        validateTopBid(lot.getId(), userId);
        validateBidValue(lot, bid.getValue());

    }

    @SuppressWarnings("WeakerAccess")
    public void validateBidValue(Lot lot, BigDecimal value) {

        if (!lot.isBidValueEnough(value)) {
            throw new BusinessException("Ошибка. Ваша ставка недостаточно высока.");
        }

    }

    @SuppressWarnings("WeakerAccess")
    public void validateTopBid(Integer lotId, Integer userId) {

        Bid topBid = bidService.findTopBidByLotId(lotId);

        if (topBid != null && userId.equals(topBid.getUser().getId())) {
            throw new BusinessException("Ошибка. Ваша ставка уже на вершине.");
        }

    }

    @SuppressWarnings("WeakerAccess")
    public void validateLotClosingDate(Lot lot) {

        if (lot.isExpired()) {
            throw new BusinessException("Ошибка. Торги по данному лоту уже окончены.");
        }

    }

    @SuppressWarnings("WeakerAccess")
    public void validateLotExistence(Lot lot) {

        if (lot == null) {
            throw new NoSuchEntityException("Ошибка. Лот по данному id не найден.");
        }

    }

}
