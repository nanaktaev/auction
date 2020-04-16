package by.company.auction.common.exceptions;

public class AlreadyExistsException extends AuctionException {

    public AlreadyExistsException(String message) {

        setMessage(message);

    }

}
