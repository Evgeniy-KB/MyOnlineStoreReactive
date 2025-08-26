package task7.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import task7.dto.OrderDto;
import task7.service.OrderService;

@Controller
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @GetMapping
    public Mono<String> getOrders(Model model){
        Flux<OrderDto> orders = orderService.findAll();
        model.addAttribute("orders", (orders));
        model.addAttribute("totalPriceOfOrders",  orders.map(OrderDto::getTotalPrice).reduce(0L, Long::sum));
        return Mono.just("orders");
    }

    @GetMapping("/{id}")
    public Mono<String> getOrder(Model model, @PathVariable("id") Long id){
        return orderService.getById(id)
                .doOnNext(x -> model.addAttribute("products", x.getProducts()))
                .doOnNext(x -> model.addAttribute("totalPriceOfOrder", x.getTotalPrice()))
                .then(Mono.just("order"));
    }
}
