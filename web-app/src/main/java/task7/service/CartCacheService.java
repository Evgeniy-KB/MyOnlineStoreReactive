package task7.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import task7.model.CartProduct;
import task7.model.Product;

public interface CartCacheService {
    Mono<Boolean> hasCacheCartByKey(Long id);

    Mono<Boolean> hasCacheCartProductByKey(Long id, Long productId);

    Mono<Boolean> setCacheCartProduct(Long cartId, Long productId, CartProduct cartProduct);

    Mono<Boolean> updateCacheCartProduct(Long cartId, Long productId, Long quantity);

    Mono<CartProduct> getCacheCartProduct(Long cartId, Long productId);

    Flux<CartProduct> getCacheCartProducts(Long cartId);

    Mono<Long> deleteCacheCart(Long cartId);

    Mono<Long> deleteCacheCartProduct(Long cartId, Long productId);
}

