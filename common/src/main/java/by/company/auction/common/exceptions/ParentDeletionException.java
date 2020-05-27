package by.company.auction.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ParentDeletionException extends AuctionException {

    public ParentDeletionException(String message) {
        setMessage(message);
    }
}
