package task6.MyOnlineStoreReactive.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import task6.MyOnlineStoreReactive.DTO.CartDTO;
import task6.MyOnlineStoreReactive.DTOTranslator.CartDTOTranslator;
import task6.MyOnlineStoreReactive.model.Cart;
import task6.MyOnlineStoreReactive.model.CartProduct;
import task6.MyOnlineStoreReactive.model.Order;
import task6.MyOnlineStoreReactive.model.Product;
import task6.MyOnlineStoreReactive.repository.CartRepository;
import task6.MyOnlineStoreReactive.repository.OrderRepository;
import task6.MyOnlineStoreReactive.repository.ProductRepository;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService{
    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    private final CartRepository cartRepository;
    @Autowired
    private final CartDTOTranslator cartDTOTranslator;

    @Autowired
    public CartServiceImpl(ProductRepository productRepository, OrderRepository orderRepository, CartRepository cartRepository, CartDTOTranslator cartDTOTranslator){
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.cartDTOTranslator = cartDTOTranslator;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CartDTO> findCurrentCart(){
        Optional<Cart> cart = cartRepository.findCurrentCart();

        return Optional.of(cart.isEmpty() ? new CartDTO() : cartDTOTranslator.ToCartDTO(cart.get()));
    }

    @Override
    @Transactional
    public void addProductQuantity(Long productId, int quantity){
        Optional<Cart> existCart = cartRepository.findCurrentCart();

        Cart cart;
        if (existCart.isPresent())
            cart = existCart.get();
        else
            cart = new Cart();

        Product product = productRepository.getById(productId);
        cart.addProductQuantity(product, quantity);

        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void deleteCart(Long cartId){
        //Очищаем корзину
        cartRepository.deleteById(cartId);
    }

    @Override
    @Transactional
    public Long buyOrder(Long cartId){
        Cart cart = cartRepository.getById(cartId);

        Order order = new Order();
        //формируем заказ из корзину
        for(CartProduct cartProduct : cart.getCartProducts()){
            order.addProductQuantity(cartProduct.getProduct(), cartProduct.getQuantity());
        }

        return orderRepository.save(order).getId();
    }

    @Override
    @Transactional
    public void deleteProductFromCart(Long cartId, Long productId){
        cartRepository.deleteByCartIdAndProductId(cartId, productId);
    }

    @Override
    @Transactional
    public void decrementProductInCart(Long cartId, Long productId){
        cartRepository.updateQuantityProductInCart(cartId, productId, -1L);
    }

    @Override
    @Transactional
    public void incrementProductInCart(Long cartId, Long productId){
        cartRepository.updateQuantityProductInCart(cartId, productId, +1L);
    }

}
