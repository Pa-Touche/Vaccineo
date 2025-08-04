package lu.pokevax.technical.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The user id could not be extracted from the JWT")
public class MissingUserIdInTokenException extends RuntimeException {
    public MissingUserIdInTokenException(String message) {
        super(message);
    }
}