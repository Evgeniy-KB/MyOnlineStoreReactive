package task6.MyOnlineStoreReactive.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import task6.MyOnlineStoreReactive.DTO.CartDTO;
import task6.MyOnlineStoreReactive.DTOTranslator.CartDTOTranslator;
import task6.MyOnlineStoreReactive.model.*;
import task6.MyOnlineStoreReactive.repository.*;

@Service
public class CartServiceImpl implements CartService{
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
    private final CartDTOTranslator cartDTOTranslator;

    @Autowired
    public CartServiceImpl(ProductRepository productRepository, OrderRepository orderRepository, CartRepository cartRepository,
                           CartProductRepository cartProductRepository, ProductOrderRepository productOrderRepository,
                           CartDTOTranslator cartDTOTranslator){
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.cartProductRepository = cartProductRepository;
        this.productOrderRepository = productOrderRepository;
        this.cartDTOTranslator = cartDTOTranslator;
    }

    @Override
    @Transactional
    public Mono<CartDTO> findCurrentCart() {
        return cartRepository.findCurrentCart().switchIfEmpty(cartRepository.save(new Cart()))
                .flatMap(cart -> cartRepository.findCartProductsByCartId(cart.getId())
                        .flatMap(cartProduct -> productRepository.findById(cartProduct.getProductId())
                                .map(product -> {
                                    cartProduct.setProduct(product);
                                    return cartProduct;
                                }))
                        .collectList()
                        .map(cart::cartWithProducts))
                .map(cartDTOTranslator::ToCartDTO);
    }

    @Override
    @Transactional
    public Mono<Long> addProductQuantity(Long productId, Long quantity){
        return cartRepository.findCurrentCart().switchIfEmpty(cartRepository.save(new Cart()))
                .flatMap(cart ->
                        Mono.zip(Mono.just(cart), cartProductRepository.findCountProductsInCart(cart.getId(), productId))
                                .flatMap(tuple -> tuple.getT2() == 0//такого товара нет в корзине
                                        ? cartProductRepository.save(new CartProduct(cart.getId(), productId, quantity))
                                        .map(cartProduct -> cart.getId())
                                        : cartProductRepository.updateCountProductsInCart(cart.getId(), productId, quantity)
                                        .map(x -> cart.getId())
                                ));
    }

    @Override
    @Transactional
    public Mono<Void> deleteCart(Long cartId){
        //Очищаем корзину
        return cartRepository.deleteById(cartId);
    }

    @Override
    @Transactional
    public Mono<Long> buyOrder(Long cartId){
        return orderRepository.save(new Order())
                .flatMap(order -> cartRepository.findById(cartId)
                        .flatMap(cart -> cartRepository.findCartProductsByCartId(cartId)
                                .flatMap(cartProduct -> /*{*/
                                    /*return*/ productOrderRepository.save(new ProductOrder(order.getId(), cartProduct.getProductId(), cartProduct.getQuantity()))
                                    /*}*/)
                                .collectList())
                        .map(order::orderWithProducts))
                .map(Order::getId);
    }

    @Override
    @Transactional
    public Mono<Void> deleteProductFromCart(Long cartId, Long productId){
        return cartRepository.deleteByCartIdAndProductId(cartId, productId);
    }

    @Override
    @Transactional
    public Mono<Void> decrementProductInCart(Long cartId, Long productId){
        return cartRepository.updateQuantityProductInCart(cartId, productId, -1L);
    }

    @Override
    @Transactional
    public Mono<Void> incrementProductInCart(Long cartId, Long productId){
        return cartRepository.updateQuantityProductInCart(cartId, productId, +1L);
    }

}
