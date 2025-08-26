package task7.service;

import reactor.core.publisher.Mono;

public interface AccountService {
    Mono<Long> getBalance();

    Mono<Boolean> payOrder(Long price);
}
