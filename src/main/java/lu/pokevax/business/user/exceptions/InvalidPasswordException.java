package lu.pokevax.business.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Email found but password was invalid")
public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String message) {
        super(message);
    }

    public static InvalidPasswordException forEmail(String email) {
        return new InvalidPasswordException(String.format("Invalid password provided for email: '%s'", email));
    }
}