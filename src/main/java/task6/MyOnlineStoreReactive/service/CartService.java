package task6.MyOnlineStoreReactive.service;

import reactor.core.publisher.Mono;
import task6.MyOnlineStoreReactive.DTO.CartDTO;

public interface CartService {

    Mono<CartDTO> findCurrentCart();

    Mono<Long> addProductQuantity(Long productId, Long quantity);

    Mono<Void> deleteCart(Long cartId);

    Mono<Long> buyOrder(Long cartId);

    Mono<Void> deleteProductFromCart(Long cartId, Long productId);

    Mono<Void> decrementProductInCart(Long cartId, Long productId);

    Mono<Void> incrementProductInCart(Long cartId, Long productId);
}
