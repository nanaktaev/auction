package by.company.auction.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotYetPopulatedException extends AuctionException {

    public NotYetPopulatedException(String message) {
        setMessage(message);
    }
}
