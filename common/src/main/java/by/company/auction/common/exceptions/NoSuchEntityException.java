package by.company.auction.common.exceptions;

public class NoSuchEntityException extends AuctionException {

    public NoSuchEntityException(String message) {
        setMessage(message);
    }

}
