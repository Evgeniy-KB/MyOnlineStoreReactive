package task7.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import task7.client.ApiClient;
import task7.client.api.BalanceApi;
import task7.client.api.PayApi;
import task7.client.model.BalanceResponse;
import task7.client.model.PayResponse;

@Service
public class PaymentServiceImpl implements PaymentService{
    @Value("${payment-service.base-path}")
    private String basePath;

    private BalanceApi balanceApi;
    private PayApi payApi;

    @PostConstruct
    public void init(){
        balanceApi = new BalanceApi(new ApiClient().setBasePath(basePath));
        payApi = new PayApi(new ApiClient().setBasePath(basePath));
    }

    @Override
    public Mono<ResponseEntity<BalanceResponse>> getBalance(){
        return balanceApi.getBalanceWithHttpInfo()
                .onErrorResume(error -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BalanceResponse().balance(0L))));
    }

    @Override
    public Mono<ResponseEntity<PayResponse>> payOrder(Long price){
        return payApi.payOrderWithHttpInfo(price)
                .onErrorResume(error -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PayResponse().success(false).message(error.getMessage()))));
    }
}
