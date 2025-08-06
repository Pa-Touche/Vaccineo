package lu.vaccineo.business.user.exceptions;

import lu.vaccineo.technical.exceptions.DisplayableRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Email found but password was invalid")
public class InvalidPasswordException extends DisplayableRuntimeException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}