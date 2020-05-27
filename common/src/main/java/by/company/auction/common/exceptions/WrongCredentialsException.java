package by.company.auction.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class WrongCredentialsException extends AuctionException {

    public WrongCredentialsException(String message) {
        setMessage(message);
    }
}
