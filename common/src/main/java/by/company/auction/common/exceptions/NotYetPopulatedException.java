package by.company.auction.common.exceptions;

public class NotYetPopulatedException extends AuctionException {

    public NotYetPopulatedException(String message) {
        setMessage(message);
    }

}
