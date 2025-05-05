package task6.MyOnlineStoreReactive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import task6.MyOnlineStoreReactive.model.Cart;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query(value = "SELECT * FROM carts c LIMIT 1", nativeQuery = true)
    Optional<Cart> findCurrentCart();

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM CartProduct cp WHERE cp.cart.id = :cart_id and cp.product.id = :product_id")
    void deleteByCartIdAndProductId(@Param("cart_id") Long cart_id, @Param("product_id") Long product_id);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE CartProduct cp SET cp.quantity = cp.quantity + :changeQuantity WHERE cp.cart.id = :cart_id and cp.product.id = :product_id")
    void updateQuantityProductInCart(@Param("cart_id") Long cart_id, @Param("product_id") Long product_id, @Param("changeQuantity") Long changeQuantity);
}
