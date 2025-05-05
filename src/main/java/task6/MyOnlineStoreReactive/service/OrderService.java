package task6.MyOnlineStoreReactive.service;

import task6.MyOnlineStoreReactive.DTO.OrderDTO;

import java.util.List;

public interface OrderService {
    OrderDTO getById(Long orderId);

    List<OrderDTO> findAll();
}
