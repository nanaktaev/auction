package by.company.auction.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class OwnershipException extends AuctionException {

    public OwnershipException(String message) {
        setMessage(message);
    }
}
