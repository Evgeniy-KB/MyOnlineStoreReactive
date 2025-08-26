package task7.service;

import reactor.core.publisher.Mono;
import task7.dto.CartDto;

public interface CartService {

    Mono<CartDto> findCartById(Long id);

    Mono<CartDto> findCurrentCart();

    Mono<Long> addProductQuantity(Long productId, Long quantity);

    Mono<Void> deleteCart(Long cartId);

    Mono<Long> buyOrder(Long cartId);

    Mono<Void> deleteProductFromCart(Long cartId, Long productId);

    Mono<Void> decrementProductInCart(Long cartId, Long productId);

    Mono<Void> incrementProductInCart(Long cartId, Long productId);
}
