package task7.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

import task7.client.model.BalanceResponse;
import task7.service.CartService;
import task7.service.PaymentService;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private final CartService cartService;
    @Autowired
    private final PaymentService paymentService;

    @Autowired
    public CartController(CartService cartService, PaymentService paymentService){
        this.cartService = cartService;
        this.paymentService = paymentService;
    }

    @GetMapping
    public Mono<String> getCartProducts(Model model){
        Mono<ResponseEntity<BalanceResponse>> responseEntity = paymentService.getBalance();
        Mono<Long> balance = responseEntity.map(HttpEntity::getBody).map(BalanceResponse::getBalance);
        Mono<Boolean> isPaymentNotAvailable = responseEntity.map(re -> re.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR);

        return cartService.findCurrentCart()
                .doOnNext(x -> model.addAttribute("products", x.getProducts()))
                .doOnNext(x -> model.addAttribute("totalPriceOfCart", x.getTotalPrice()))
                .doOnNext(x -> model.addAttribute("balance", balance))
                .doOnNext(x -> model.addAttribute("isPaymentNotAvailable", isPaymentNotAvailable))
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
                .flatMap(cart -> {
                    return Mono.zip(Mono.just(cart.getId()),    //id корзины товаров
                            Mono.just(cart.getTotalPrice()),    //цена корзины товаров
                            paymentService.payOrder(cart.getTotalPrice()).map(s -> s.getBody().getSuccess()));
                })
                .flatMap(x -> {
                    //Если баланс больше или равен цене покупки
                    if (x.getT3()){
                        return Mono.zip(Mono.just(x.getT1()),   //id корзины товаров
                                cartService.buyOrder(x.getT1()))    //id созданного заказа
                                .flatMap(y -> cartService.deleteCart(y.getT1()).then(Mono.just("redirect:/orders/" + y.getT2())));
                    }
                    else {
                        return Mono.just("redirect:/cart");
                    }
                });
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
