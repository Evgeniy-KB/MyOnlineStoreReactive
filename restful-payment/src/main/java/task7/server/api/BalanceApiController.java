package task7.server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import task7.server.model.BalanceResponse;
import task7.service.AccountService;

import javax.validation.constraints.*;
import javax.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-06-22T10:56:38.586196200+05:00[Asia/Yekaterinburg]", comments = "Generator version: 7.12.0")
@RestController("/api/balance")
public class BalanceApiController implements BalanceApi {

    @Autowired
    private AccountService accountService;

    @Autowired
    public BalanceApiController(AccountService accountService){
        this.accountService = accountService;
    }

    @Override
    public Mono<ResponseEntity<BalanceResponse>> getBalance(ServerWebExchange exchange) {
        return accountService.getBalance()
                .map(balance -> ResponseEntity.status(HttpStatus.OK).body(new BalanceResponse().balance(balance)));
    }
}
