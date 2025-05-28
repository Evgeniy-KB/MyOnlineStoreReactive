package task6.MyOnlineStoreReactive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Mono;
import task6.MyOnlineStoreReactive.DTO.CartDTO;
import task6.MyOnlineStoreReactive.repository.OrderRepository;
import task6.MyOnlineStoreReactive.service.CartService;

import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService){
        this.cartService = cartService;
    }

    @GetMapping
    public Mono<String> getCartProducts(Model model){
        return cartService.findCurrentCart()
                .doOnNext(x -> model.addAttribute("products", x.getProducts()))
                .doOnNext(x -> model.addAttribute("totalPriceOfCart", x.getTotalPrice()))
                .map(x -> "cart");
    }

    @PostMapping(value = "/product/{id}/delete")
    public Mono<String> deleteProduct(@PathVariable("id") Long id) {
        return cartService.findCurrentCart()
                .flatMap(cart -> cartService.deleteProductFromCart(cart.getId(), id))
                .then(Mono.just("redirect:/cart"));
    }

    @PostMapping("/buy-order")
    public Mono<String> buyOrder(){
        return cartService.findCurrentCart()
                .flatMap(x -> {
                    Long cart_id = x.getId();
                    Mono<Long> order_id = cartService.buyOrder(cart_id);
                    return Mono.zip(Mono.just(cart_id), order_id);
                })
                .flatMap(x -> cartService.deleteCart(x.getT1())
                        .then(Mono.just("redirect:/orders/" + x.getT2())));
    }

    @PostMapping("/product/{id}/decrement")
    public Mono<String> decrementProductInCart(@PathVariable("id") Long id){
        return cartService.findCurrentCart().flatMap(x -> cartService.decrementProductInCart(x.getId(), id))
                .then(Mono.just("redirect:/cart"));
    }

    @PostMapping("/product/{id}/increment")
    public Mono<String> incrementProductInCart(@PathVariable("id") Long id){
        return cartService.findCurrentCart().flatMap(x -> cartService.incrementProductInCart(x.getId(), id))
                .then(Mono.just("redirect:/cart"));
    }
}
