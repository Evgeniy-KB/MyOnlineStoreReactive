package task6.MyOnlineStoreReactive.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import task6.MyOnlineStoreReactive.model.Product;
import task6.MyOnlineStoreReactive.model.ProductOrder;

@Repository
public interface ProductRepository extends R2dbcRepository<Product, Long> {
    @Query("SELECT COUNT(1) FROM products p WHERE LOWER(p.title) LIKE CONCAT('%',LOWER(:sampleSearch),'%') or LOWER(p.description) LIKE CONCAT('%',LOWER(:sampleSearch),'%')")
    Mono<Long> findCountByFilter(@Param("sampleSearch") String sampleSearch);

    @Query("SELECT p.* FROM products p WHERE LOWER(p.title) LIKE CONCAT('%',LOWER(:sampleSearch),'%') or LOWER(p.description) LIKE CONCAT('%',LOWER(:sampleSearch),'%')")
    Flux<Product> findAllWithPaginationByFilter(@Param("sampleSearch") String sampleSearch, Pageable pageable);

    @Query("SELECT p.* FROM products p WHERE LOWER(p.title) LIKE CONCAT('%',LOWER(:sampleSearch),'%') or LOWER(p.description) LIKE CONCAT('%',LOWER(:sampleSearch),'%')")
    Flux<Product> findAllByFilter(@Param("sampleSearch") String sampleSearch);

    @Query("SELECT * FROM product_order po WHERE po.order_id = :order_id ORDER BY po.product_id")
    Flux<ProductOrder> findOrderProductsByOrderId(@Param("order_id") Long order_id);
}
