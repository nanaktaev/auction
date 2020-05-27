package by.company.auction.common.exceptions;

public abstract class AuctionException extends RuntimeException {

    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    void setMessage(String message) {
        this.message = message;
    }
}
