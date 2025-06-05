package task6.MyOnlineStoreReactive.DTOTranslator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task6.MyOnlineStoreReactive.DTO.CartDTO;
import task6.MyOnlineStoreReactive.model.Cart;

@Service
public class CartDTOTranslator {
    private final ProductDTOTranslator productDTOTranslator;

    @Autowired
    public CartDTOTranslator(ProductDTOTranslator productDTOTranslator){
        this.productDTOTranslator = productDTOTranslator;
    }

    public CartDTO ToCartDTO(Cart cart){
        return new CartDTO(cart.getId(), cart.getCartProducts().stream().map(productDTOTranslator::ToProductDTO).toList());
    }

    public Cart ToCart(CartDTO cartDTO){
        return new Cart(cartDTO.getId());
    }
}
