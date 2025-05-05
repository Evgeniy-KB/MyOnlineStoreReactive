package task6.MyOnlineStoreReactive.service;

import task6.MyOnlineStoreReactive.DTO.CartDTO;

import java.util.Optional;

public interface CartService {

    Optional<CartDTO> findCurrentCart();

    void addProductQuantity(Long productId, int quantity);

    void deleteCart(Long cartId);

    Long buyOrder(Long cartId);

    void deleteProductFromCart(Long cartId, Long productId);

    void decrementProductInCart(Long cartId, Long productId);

    void incrementProductInCart(Long cartId, Long productId);
}
