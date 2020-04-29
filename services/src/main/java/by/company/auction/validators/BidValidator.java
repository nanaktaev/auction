package by.company.auction.validators;

import by.company.auction.exceptions.AuctionException;
import by.company.auction.model.Bid;
import by.company.auction.model.Lot;
import by.company.auction.services.BidService;

public class BidValidator {

    public void validate(Lot lot, Bid bid, Integer userId) {

        if (lot == null) {
            throw new AuctionException("Ошибка. Лот по данному id не найден.");
        }
        if (lot.isExpired()) {
            throw new AuctionException("Ошибка. Торги по данному лоту уже окончены.");
        }

        Bid topBid = BidService.getInstance().findTopBidByLotId(lot.getId());

        if (topBid != null && userId.equals(topBid.getUserId())) {
            throw new AuctionException("Ошибка. Ваша ставка уже на вершине.");
        }

        if (!lot.isBidValueEnough(bid.getValue())) {
            throw new AuctionException("Ошибка. Ваша ставка недостаточно высока.");
        }

    }

    private static BidValidator bidValidatorInstance;

    private BidValidator() {
    }

    public static BidValidator getInstance() {
        if (bidValidatorInstance != null) {
            return bidValidatorInstance;
        }
        bidValidatorInstance = new BidValidator();
        return bidValidatorInstance;
    }
}
