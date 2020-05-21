package by.company.auction.common.exceptions;

public class WrongCredentialsException extends AuctionException {

    public WrongCredentialsException(String message) {
        setMessage(message);
    }

}
