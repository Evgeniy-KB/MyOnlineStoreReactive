package task7.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task7.dto.CartDto;
import task7.model.Cart;

@Service
public class CartMapper {
    private final ProductMapper productMapper;

    @Autowired
    public CartMapper(ProductMapper productMapper){
        this.productMapper = productMapper;
    }

    public CartDto ToCartDto(Cart cart){
        return new CartDto(cart.getId(), cart.getCartProducts().stream().map(productMapper::ToProductDto).toList());
    }

    public Cart ToCart(CartDto cartDto){
        return new Cart(cartDto.getId());
    }
}
