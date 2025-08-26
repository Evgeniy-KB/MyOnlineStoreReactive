package task7.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task7.dto.OrderDto;
import task7.model.Order;

@Service
public class OrderMapper {
    private final ProductMapper productMapper;

    @Autowired
    public OrderMapper(ProductMapper productMapper){
        this.productMapper = productMapper;
    }

    public OrderDto ToOrderDto(Order order){
        return new OrderDto(order.getId(), order.getProductOrders().stream().map(productMapper::ToProductDto).toList());
    }

}
