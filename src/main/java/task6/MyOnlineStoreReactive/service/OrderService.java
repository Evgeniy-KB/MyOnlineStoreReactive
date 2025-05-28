package task6.MyOnlineStoreReactive.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import task6.MyOnlineStoreReactive.DTO.OrderDTO;

public interface OrderService {
    Mono<OrderDTO> getById(Long orderId);

    Flux<OrderDTO> findAll();
}
