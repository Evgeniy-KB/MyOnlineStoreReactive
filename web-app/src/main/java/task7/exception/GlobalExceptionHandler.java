package task7.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public Mono<ResponseEntity<String>> globalExceptionHandler( Exception ex) {
        log.error("exception:  {}, message:  {}", ex.getClass().getName(), ex.getLocalizedMessage());

        return Mono.just(ResponseEntity.badRequest().body("exception:  " + ex.getClass().getName() + ", message:  " + ex.getLocalizedMessage() ));
    }
}
