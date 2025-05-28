package task6.MyOnlineStoreReactive.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import task6.MyOnlineStoreReactive.model.Order;

@Repository
public interface OrderRepository extends R2dbcRepository<Order, Long> {
    /*@Query("INSERT INTO product_order(order_id,product_id,quantity) values(:order_id,:product_id,:quantity)")
    Mono<Long> addProductInOrder(@Param("order_id") Long order_id, @Param("product_id") Long product_id, @Param("quantity") Long quantity);*/
}
