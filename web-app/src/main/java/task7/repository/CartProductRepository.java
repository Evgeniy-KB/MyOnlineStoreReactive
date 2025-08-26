package task7.repository;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import task7.model.CartProduct;

@Repository
public interface CartProductRepository extends R2dbcRepository<CartProduct, Long> {
    @Query("SELECT COUNT(1) FROM cart_product cp WHERE cp.cart_id = :cart_id and cp.product_id = :product_id")
    Mono<Long> findCountProductsInCart(@Param("cart_id") Long cart_id, @Param("product_id") Long product_id);

    @Modifying
    @Query("UPDATE cart_product cp SET cp.quantity = cp.quantity + :quantity WHERE cp.cart_id = :cart_id and cp.product_id = :product_id")
    Mono<Long> updateCountProductsInCart(@Param("cart_id") Long cart_id, @Param("product_id") Long product_id, @Param("quantity") Long quantity );

    @Query("SELECT cp.* FROM cart_product cp WHERE cp.cart_id = :cart_id and cp.product_id = :product_id")
    Mono<CartProduct> findCartProduct(@Param("cart_id") Long cart_id, @Param("product_id") Long product_id);
}
