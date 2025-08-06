package lu.pokevax.technical.web;

import lombok.extern.slf4j.Slf4j;
import lu.pokevax.technical.exceptions.DisplayableRuntimeException;
import lu.pokevax.technical.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleConstraintViolationException(MethodArgumentNotValidException ex) {
        log.info("Error within client payload", ex);
        // TODO: create something better than this.
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.warn("Following exception occured, as this is a instance of DisplayableRuntimeException the message will be returned as it to the client", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }


    @ExceptionHandler(DisplayableRuntimeException.class)
    public ResponseEntity<String> handleInvalidPasswordException(DisplayableRuntimeException ex) {
        log.warn("Following exception occured, as this is a instance of DisplayableRuntimeException the message will be returned as it to the client", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
}
