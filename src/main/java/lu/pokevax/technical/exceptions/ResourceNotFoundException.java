package lu.pokevax.technical.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource with given identifier was not found")
public class ResourceNotFoundException extends DisplayableRuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
