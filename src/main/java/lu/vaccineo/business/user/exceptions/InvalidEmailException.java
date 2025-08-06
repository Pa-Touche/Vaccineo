package lu.vaccineo.business.user.exceptions;

import lu.vaccineo.technical.exceptions.DisplayableRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class InvalidEmailException extends DisplayableRuntimeException {
    public InvalidEmailException(String message) {
        super(message);
    }

    public static InvalidEmailException forEmail(String email) {
        return new InvalidEmailException(String.format("No account found related to: '%s'", email));
    }
}