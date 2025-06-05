package task6.MyOnlineStoreReactive.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "cart_product")
public class CartProduct {
    @Id
    private Long id;

    @Transient
    private Product product;

    public void setProduct(Product product){
        this.product = product;
    }

    @Transient
    private Cart cart;

    @Column("cart_id")
    private Long cartId;

    @Column("product_id")
    private Long productId;

    public Long getProductId(){
        return this.productId;
    }

    @Column("quantity")
    private Long quantity;

    public Long getId(){
        return this.id;
    }

    public Cart getCart(){
        return this.cart;
    }

    public Product getProduct(){
        return this.product;
    }

    public Long getQuantity(){
        return this.quantity;
    }

    public void setQuantity(Long quantity){
        this.quantity = quantity;
    }

    public CartProduct(){}

    public CartProduct(Long cartId, Long productId, Long quantity){
        this.cartId = cartId;
        this.productId = productId;
        this.quantity = quantity;
    }
}
