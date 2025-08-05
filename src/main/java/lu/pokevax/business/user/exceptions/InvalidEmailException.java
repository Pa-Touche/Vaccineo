package lu.pokevax.business.user.exceptions;

import lu.pokevax.technical.exceptions.DisplayableRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Email not found in the system")
public class InvalidEmailException extends DisplayableRuntimeException {
    public InvalidEmailException(String message) {
        super(message);
    }

    public static InvalidEmailException forEmail(String email) {
        return new InvalidEmailException(String.format("No account found related to: '%s'", email));
    }
}