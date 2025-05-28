package task6.MyOnlineStoreReactive.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import task6.MyOnlineStoreReactive.DTO.OrderDTO;
import task6.MyOnlineStoreReactive.DTOTranslator.OrderDTOTranslator;
import task6.MyOnlineStoreReactive.repository.OrderRepository;
import task6.MyOnlineStoreReactive.repository.ProductRepository;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final OrderDTOTranslator orderDTOTranslator;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, OrderDTOTranslator orderDTOTranslator){
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderDTOTranslator = orderDTOTranslator;
    }

    @Override
    @Transactional
    public Mono<OrderDTO> getById(Long orderId){
        var result = orderRepository.findById(orderId)
                .flatMap(order -> productRepository.findOrderProductsByOrderId(orderId)
                        .flatMap(productOrder -> productRepository.findById(productOrder.getProductId())
                                .map(product -> {
                                    productOrder.setProduct(product);
                                    return productOrder;
                                }))
                        .collectList()
                        .map(order::orderWithProducts));

        return result.map(orderDTOTranslator::ToOrderDTO);
    }

    @Override
    @Transactional
    public Flux<OrderDTO> findAll(){
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

        return result.map(orderDTOTranslator::ToOrderDTO);
    }
}
