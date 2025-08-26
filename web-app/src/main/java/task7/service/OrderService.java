package task7.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import task7.dto.OrderDto;

public interface OrderService {
    Mono<OrderDto> getById(Long orderId);

    Flux<OrderDto> findAll();
}
