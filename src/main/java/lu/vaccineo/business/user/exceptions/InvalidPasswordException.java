package lu.vaccineo.business.user.exceptions;

import lu.vaccineo.technical.exceptions.DisplayableRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class InvalidPasswordException extends DisplayableRuntimeException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}