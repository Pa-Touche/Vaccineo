package lu.vaccineo.technical.exceptions;


/**
 * Parent class for exceptions where the exception's message can be displayed as is.
 * <p>
 * This is a quick-dirty solution and is not meant to stay.
 */
public class DisplayableRuntimeException extends RuntimeException {
    public DisplayableRuntimeException(String message) {
        super(message);
    }
}
