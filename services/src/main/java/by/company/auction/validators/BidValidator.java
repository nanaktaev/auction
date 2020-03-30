package by.company.auction.validators;

import by.company.auction.model.Bid;
import by.company.auction.model.Lot;
import by.company.auction.services.BidService;

public class BidValidator {

    public static void validate(Lot lot, Bid bid, Integer userId) {

        if (lot == null) {
            throw new IllegalStateException("Ошибка. Лот по данному id не найден.");
        }
        if (lot.isExpired()) {
            throw new IllegalStateException("Ошибка. Торги по данному лоту уже окончены.");
        }

        Bid topBid = BidService.getInstance().findTopBid(lot);

        if (topBid != null && userId.equals(topBid.getUserId())) {
            throw new IllegalStateException("Ошибка. Ваша ставка уже на вершине.");
        }

        if (!lot.isBidValueEnough(bid.getValue())) {
            throw new IllegalStateException("Ошибка. Ваша ставка недостаточно высока.");
        }

    }
}
