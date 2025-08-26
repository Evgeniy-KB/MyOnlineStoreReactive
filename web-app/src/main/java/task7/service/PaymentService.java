package task7.service;


import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import task7.client.model.BalanceResponse;
import task7.client.model.PayResponse;

public interface PaymentService {
    Mono<ResponseEntity<BalanceResponse>> getBalance();

    Mono<ResponseEntity<PayResponse>> payOrder(Long price);
}
