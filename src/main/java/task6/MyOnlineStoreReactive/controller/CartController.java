package task6.MyOnlineStoreReactive.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import task6.MyOnlineStoreReactive.DTO.CartDTO;
import task6.MyOnlineStoreReactive.service.CartService;

import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService){
        this.cartService = cartService;
    }

    @GetMapping
    public String getCartProducts(Model model){
        Optional<CartDTO> optionalCart = cartService.findCurrentCart();

        if (optionalCart.isPresent()){
            CartDTO cart = optionalCart.get();

            model.addAttribute("products", cart.getProducts());
            model.addAttribute("totalPriceOfCart", cart.getTotalPrice());
        }

        return "cart";
    }

    @PostMapping(value = "/product/{id}/delete")
    public String deleteProduct(@PathVariable("id") Long id) {
        Optional<CartDTO> cart = cartService.findCurrentCart();

        cart.ifPresent(c -> cartService.deleteProductFromCart(c.getId(), id));

        return "redirect:/cart";
    }

    @PostMapping("/buy-order")
    public String buyOrder(){
        Optional<CartDTO> cart = cartService.findCurrentCart();

        Long order_id = 0L;
        if (cart.isPresent()){
            Long cart_id = cart.get().getId();
            order_id = cartService.buyOrder(cart_id);
            cartService.deleteCart(cart_id);
        }

        return "redirect:/orders/" + order_id;
    }

    @PostMapping("/product/{id}/decrement")
    public String decrementProductInCart(@PathVariable("id") Long id){
        Optional<CartDTO> cart = cartService.findCurrentCart();

        cart.ifPresent(c -> cartService.decrementProductInCart(c.getId(), id));

        return "redirect:/cart";
    }

    @PostMapping("/product/{id}/increment")
    public String incrementProductInCart(@PathVariable("id") Long id){
        Optional<CartDTO> cart = cartService.findCurrentCart();

        cart.ifPresent(c -> cartService.incrementProductInCart(c.getId(), id));

        return "redirect:/cart";
    }
}
