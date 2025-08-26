package task7.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import task7.dto.CartDto;
import task7.mapper.CartMapper;
import task7.model.*;
import task7.repository.*;

@Service
public class CartServiceImpl implements CartService{
    //private static final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);

    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    private final CartRepository cartRepository;
    @Autowired
    private final CartProductRepository cartProductRepository;
    @Autowired
    private final ProductOrderRepository productOrderRepository;
    @Autowired
    private final CartMapper cartMapper;
    @Autowired
    private final CartCacheService cartCacheService;

    @Autowired
    public CartServiceImpl(ProductRepository productRepository, OrderRepository orderRepository, CartRepository cartRepository,
                           CartProductRepository cartProductRepository, ProductOrderRepository productOrderRepository,
                           CartMapper cartMapper,
                           CartCacheService cartCacheService){
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.cartProductRepository = cartProductRepository;
        this.productOrderRepository = productOrderRepository;
        this.cartMapper = cartMapper;
        this.cartCacheService = cartCacheService;
    }

    @Override
    @Transactional
    public Mono<CartDto> findCartById(Long id){
        return cartRepository.findCartById(id).map(cartMapper::ToCartDto);
    }

    @Override
    @Transactional
    public Mono<CartDto> findCurrentCart() {
        return cartRepository.findCurrentCart().switchIfEmpty(cartRepository.save(new Cart()))
                .flatMap(cart -> cartCacheService.hasCacheCartByKey(cart.getId())
                        .flatMap(hasKey -> {
                            if (hasKey){
                                return cartCacheService.getCacheCartProducts(cart.getId()).collectList();
                            }
                            else {
                                return cartRepository.findCartProductsByCartId(cart.getId())
                                        .flatMap(cartProduct -> productRepository.findById(cartProduct.getProductId())
                                                .flatMap(product -> {
                                                    cartProduct.setProduct(product);
                                                    return Mono.zip(Mono.just(cartProduct),
                                                            cartCacheService.setCacheCartProduct(cart.getId(), product.getId(), cartProduct));
                                                }))
                                        .map(Tuple2::getT1).collectList();
                            }
                        }).map(cart::cartWithProducts))
                .map(cartMapper::ToCartDto);
    }

    @Override
    @Transactional()
    public Mono<Long> addProductQuantity(Long productId, Long quantity){
        return cartRepository.findCurrentCart().switchIfEmpty(cartRepository.save(new Cart()))
                .flatMap(cart ->
                        Mono.zip(Mono.just(cart), cartProductRepository.findCountProductsInCart(cart.getId(), productId))
                                .flatMap(tuple -> {
                                    if (tuple.getT2() == 0L) {//такого товара нет в корзине
                                        return Mono.zip(cartProductRepository.save(new CartProduct(cart.getId(), productId, quantity)), productRepository.findById(productId))
                                                .flatMap(x ->{
                                                    CartProduct cartProduct = x.getT1();
                                                    Product product = x.getT2();
                                                    cartProduct.setProduct(product);
                                                    return cartCacheService.setCacheCartProduct(cart.getId(), productId, cartProduct);
                                                });
                                    }
                                    else {
                                        return cartProductRepository.updateCountProductsInCart(cart.getId(), productId, quantity)
                                                .flatMap(x -> cartCacheService.updateCacheCartProduct(cart.getId(), productId, quantity));
                                    }
                                }).map(x ->cart.getId())
                );
    }

    @Override
    @Transactional
    public Mono<Void> deleteCart(Long cartId){
        //Очищаем корзину
        return cartCacheService.deleteCacheCart(cartId)
                .flatMap(r -> cartRepository.deleteById(cartId)).then();
    }

    @Override
    @Transactional
    public Mono<Long> buyOrder(Long cartId){

        return orderRepository.save(new Order())
                .flatMap(order -> cartRepository.findById(cartId)
                        .flatMap(cart -> cartRepository.findCartProductsByCartId(cartId)
                                .flatMap(cartProduct -> productOrderRepository.save(new ProductOrder(order.getId(), cartProduct.getProductId(), cartProduct.getQuantity())))
                                .collectList())
                        .map(order::orderWithProducts))
                .map(Order::getId);
    }

    @Override
    @Transactional
    public Mono<Void> deleteProductFromCart(Long cartId, Long productId){
        return cartCacheService.deleteCacheCartProduct(cartId, productId)
                .flatMap(r -> cartRepository.deleteByCartIdAndProductId(cartId, productId)).then();
    }

    @Override
    @Transactional
    public Mono<Void> decrementProductInCart(Long cartId, Long productId){
        return cartCacheService.getCacheCartProduct(cartId, productId)
                .flatMap(cp -> {
                    cp.setQuantity(cp.getQuantity() - 1L);
                    return Mono.zip(cartCacheService.setCacheCartProduct(cartId, productId, cp), cartRepository.updateQuantityProductInCart(cartId, productId, -1L) );
                }).then();
    }

    @Override
    @Transactional
    public Mono<Void> incrementProductInCart(Long cartId, Long productId){
        return cartCacheService.getCacheCartProduct(cartId, productId)
                .flatMap(cp -> {
                    cp.setQuantity(cp.getQuantity() + 1L);
                    return Mono.zip(cartCacheService.setCacheCartProduct(cartId, productId, cp), cartRepository.updateQuantityProductInCart(cartId, productId, +1L) );
                }).then();
    }

}
