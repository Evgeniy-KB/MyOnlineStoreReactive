package task7.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import task7.model.CartProduct;

import java.time.Duration;

@Service
public class CartCacheServiceImpl implements CartCacheService{
    //private static final Logger log = LoggerFactory.getLogger(CartCacheServiceImpl.class);

    @Autowired
    private final ReactiveRedisTemplate<String, CartProduct> cartReactiveRedisTemplate;

    @Value("${spring.cache.redis.time-to-live-seconds}")
    private Long redisTtlSeconds;  //время жизни кэша в секундах

    public final String cartKey = "carts::cart";

    @Autowired
    public CartCacheServiceImpl(ReactiveRedisTemplate<String, CartProduct> cartReactiveRedisTemplate){
        this.cartReactiveRedisTemplate = cartReactiveRedisTemplate;
    }

    @Override
    public Mono<Boolean> hasCacheCartByKey(Long id){
        return cartReactiveRedisTemplate.hasKey(cartKey + id);
    }

    @Override
    public Mono<Boolean> hasCacheCartProductByKey(Long id, Long productId){
        return cartReactiveRedisTemplate.opsForHash().hasKey(cartKey + id, productId);
    }

    @Override
    public Mono<Boolean> setCacheCartProduct(Long cartId, Long productId, CartProduct cartProduct){
        return cartReactiveRedisTemplate.opsForHash().put(cartKey + cartId, productId, cartProduct)
                .flatMap(b -> cartReactiveRedisTemplate.expire(cartKey + cartId, Duration.ofSeconds(redisTtlSeconds)));//обновим кэш всей корзины
    }

    @Override
    public Mono<Boolean> updateCacheCartProduct(Long cartId, Long productId, Long quantity){
        return cartReactiveRedisTemplate.opsForHash().get(cartKey + cartId, productId)
                .flatMap(cp -> {
                    CartProduct cartProduct = (CartProduct)cp;
                    Long oldQuantity = cartProduct.getQuantity();
                    cartProduct.setQuantity(oldQuantity + quantity);
                    return cartReactiveRedisTemplate.opsForHash().put(cartKey + cartId, productId, cartProduct);
                })
                .flatMap(b -> cartReactiveRedisTemplate.expire(cartKey + cartId, Duration.ofSeconds(redisTtlSeconds)));//обновим кэш всей корзины*/
    }

    @Override
    public Mono<CartProduct> getCacheCartProduct(Long cartId, Long productId){
        return cartReactiveRedisTemplate.opsForHash().get(cartKey + cartId, productId).map(cp -> (CartProduct)cp);
    }

    @Override
    public Flux<CartProduct> getCacheCartProducts(Long cartId){
        return cartReactiveRedisTemplate.opsForHash().values(cartKey + cartId).map(cp -> (CartProduct)cp);
    }

    @Override
    public Mono<Long> deleteCacheCart(Long cartId){
        return cartReactiveRedisTemplate.delete(cartKey + cartId);
    }

    @Override
    public Mono<Long> deleteCacheCartProduct(Long cartId, Long productId) {
        return cartReactiveRedisTemplate.opsForHash().remove(cartKey + cartId, productId);
    }


}
