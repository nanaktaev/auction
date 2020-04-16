package by.company.auction.validators;

import by.company.auction.common.exceptions.BusinessException;
import by.company.auction.common.exceptions.NotFoundException;
import by.company.auction.model.Bid;
import by.company.auction.model.Lot;
import by.company.auction.services.BidService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

public class BidValidator {

    private final Logger LOGGER = LogManager.getLogger(BidValidator.class);

    public void validate(Lot lot, Bid bid, Integer userId) {

        LOGGER.debug("validate() lot = {}, bid = {}, userId = {}", lot, bid, userId);

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

        Bid topBid = BidService.getInstance().findTopBidByLotId(lotId);

        if (topBid != null && userId.equals(topBid.getUserId())) {
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
            throw new NotFoundException("Ошибка. Лот по данному id не найден.");
        }

    }

    private static BidValidator bidValidatorInstance;

    public static BidValidator getInstance() {

        if (bidValidatorInstance != null) {
            return bidValidatorInstance;
        }
        bidValidatorInstance = new BidValidator();
        return bidValidatorInstance;

    }

}
