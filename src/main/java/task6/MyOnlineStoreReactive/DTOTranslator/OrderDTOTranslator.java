package task6.MyOnlineStoreReactive.DTOTranslator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task6.MyOnlineStoreReactive.DTO.OrderDTO;
import task6.MyOnlineStoreReactive.model.Order;

@Service
public class OrderDTOTranslator {
    private final ProductDTOTranslator productDTOTranslator;

    @Autowired
    public OrderDTOTranslator(ProductDTOTranslator productDTOTranslator){
        this.productDTOTranslator = productDTOTranslator;
    }

    public OrderDTO ToOrderDTO(Order order){
        return new OrderDTO(order.getId(), order.getProductOrders().stream().map(productDTOTranslator::ToProductDTO).toList());
    }

}
