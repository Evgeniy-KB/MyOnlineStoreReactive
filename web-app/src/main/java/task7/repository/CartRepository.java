package task7.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import task7.model.Cart;
import task7.model.CartProduct;

@Repository
public interface CartRepository extends R2dbcRepository<Cart, Long> {
    @Query("SELECT c.* FROM carts c WHERE id = :id")
    Mono<Cart> findCartById(@Param("id") Long id);

    @Query("SELECT c.* FROM carts c LIMIT 1")
    Mono<Cart> findCurrentCart();

    @Modifying
    @Query("DELETE FROM cart_product cp WHERE cp.cart_id = :cart_id and cp.product_id = :product_id")
    Mono<Void> deleteByCartIdAndProductId(@Param("cart_id") Long cart_id, @Param("product_id") Long product_id);

    @Modifying
    @Query("UPDATE cart_product cp SET cp.quantity = cp.quantity + :changeQuantity WHERE cp.cart_id = :cart_id and cp.product_id = :product_id")
    Mono<Void> updateQuantityProductInCart(@Param("cart_id") Long cart_id, @Param("product_id") Long product_id, @Param("changeQuantity") Long changeQuantity);

    @Query("SELECT * FROM cart_product cp WHERE cp.cart_id = :cart_id ORDER BY cp.product_id")
    Flux<CartProduct> findCartProductsByCartId(@Param("cart_id") Long cart_id);
}
