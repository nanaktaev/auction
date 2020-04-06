package by.company.auction.dao;

import by.company.auction.model.Bid;

public class BidDao extends AbstractDao<Bid> {

    private static BidDao bidDaoInstance;

    private BidDao() {
        super(Bid.class);
    }

    public static BidDao getInstance() {
        if (bidDaoInstance != null) {
            return bidDaoInstance;
        }
        bidDaoInstance = new BidDao();
        return bidDaoInstance;
    }
}
