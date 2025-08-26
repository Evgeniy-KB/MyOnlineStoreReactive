package task7.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import task7.dto.OrderDto;
import task7.mapper.OrderMapper;
import task7.repository.OrderRepository;
import task7.repository.ProductRepository;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final OrderMapper orderMapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, OrderMapper orderMapper){
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional
    public Mono<OrderDto> getById(Long orderId){
        var result = orderRepository.findById(orderId)
                .flatMap(order -> productRepository.findOrderProductsByOrderId(orderId)
                        .flatMap(productOrder -> productRepository.findById(productOrder.getProductId())
                                .map(product -> {
                                    productOrder.setProduct(product);
                                    return productOrder;
                                }))
                        .collectList()
                        .map(order::orderWithProducts));

        return result.map(orderMapper::ToOrderDto);
    }

    @Override
    @Transactional
    public Flux<OrderDto> findAll(){
        var result =  orderRepository.findAll()
                .flatMap(order -> productRepository.findOrderProductsByOrderId(order.getId())
                        .flatMap(productOrder -> productRepository.findById(productOrder.getProductId())
                                .map(product -> {
                                    productOrder.setProduct(product);
                                    return productOrder;
                                }))
                        .collectList()
                        .map(order::orderWithProducts)
                );

        return result.map(orderMapper::ToOrderDto);
    }
}
