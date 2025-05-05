package task6.MyOnlineStoreReactive.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import task6.MyOnlineStoreReactive.DTO.OrderDTO;
import task6.MyOnlineStoreReactive.DTOTranslator.OrderDTOTranslator;
import task6.MyOnlineStoreReactive.model.Order;
import task6.MyOnlineStoreReactive.repository.OrderRepository;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private final OrderDTOTranslator orderDTOTranslator;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderDTOTranslator orderDTOTranslator){
        this.orderRepository = orderRepository;
        this.orderDTOTranslator = orderDTOTranslator;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getById(Long orderId){
        Order order =  orderRepository.getById(orderId);
        return orderDTOTranslator.ToOrderDTO(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> findAll(){
        return orderRepository.findAll().stream().map(orderDTOTranslator::ToOrderDTO).toList();
    }
}
