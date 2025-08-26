package task7.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InsufficientBalanceException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleInsufficientBalanceException(InsufficientBalanceException ex) {

        log.error("GlobalExceptionHandler: {},  message:  {}", ex.getClass().getName(), ex.getMessage());

        ErrorResponse response = new ErrorResponse(ex.getMessage());
        return Mono.just(new ResponseEntity<>(response, HttpStatus.BAD_REQUEST));

    }
}
