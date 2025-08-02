package lu.pokevax.business.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Email not found in the system")
public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String message) {
        super(message);
    }

    public static InvalidEmailException forEmail(String email) {
        return new InvalidEmailException(String.format("No account found related to: '%s'", email));
    }
}