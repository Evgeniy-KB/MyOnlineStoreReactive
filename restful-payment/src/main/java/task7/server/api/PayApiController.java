package task7.server.api;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import task7.exception.InsufficientBalanceException;
import task7.server.model.PayResponse;
import task7.service.AccountService;

import javax.validation.constraints.*;
import javax.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-06-22T10:56:38.586196200+05:00[Asia/Yekaterinburg]", comments = "Generator version: 7.12.0")
@RestController("/api/pay")
public class PayApiController implements PayApi {

    private static final Logger log = LoggerFactory.getLogger(PayApiController.class);

    @Autowired
    private final AccountService accountService;

    @Autowired
    public PayApiController(AccountService accountService){
        this.accountService = accountService;
    }

    @Override
    public Mono<ResponseEntity<PayResponse>> payOrder(@RequestParam(name = "price", required = true) Long price, ServerWebExchange exchange){
        return accountService.payOrder(price)
                .onErrorResume(InsufficientBalanceException.class, ex -> {
                    log.error("error: {}  AND message:   {}", ex.getClass().getName(), ex.getLocalizedMessage());
                    return Mono.just(false);
                })
                .map(paid -> {
                    if (paid) {
                        log.error("Заказ оформлен");
                        return ResponseEntity.status(HttpStatus.OK).body(new PayResponse().success(true).message("Заказ оформлен"));
                    }

                    log.error("Недостаточно средств для покупки");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PayResponse().success(false).message("Недостаточно средств для покупки"));
                });
    }
}
