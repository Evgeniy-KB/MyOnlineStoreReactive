package task6.MyOnlineStoreReactive.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import task6.MyOnlineStoreReactive.DTO.OrderDTO;
import task6.MyOnlineStoreReactive.DTOTranslator.OrderDTOTranslator;
import task6.MyOnlineStoreReactive.service.OrderService;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService, OrderDTOTranslator orderDTOTranslator){
        this.orderService = orderService;
    }

    @GetMapping
    public String getOrders(Model model){
        List<OrderDTO> orders = orderService.findAll();
        model.addAttribute("orders", orders);

        int totalPriceOfOrders = orders.stream().mapToInt(OrderDTO::getTotalPrice).sum();
        model.addAttribute("totalPriceOfOrders", totalPriceOfOrders);

        return "orders";
    }

    @GetMapping("/{id}")
    public String getOrder(Model model, @PathVariable("id") Long id){
        OrderDTO orderDTO = orderService.getById(id);

        model.addAttribute("products", orderDTO.getProducts());
        model.addAttribute("totalPriceOfOrder", orderDTO.getTotalPrice());

        return "order";
    }



}
