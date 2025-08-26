package task7.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "product_order")
public class ProductOrder {
    @Id
    private Long id;

    public Long getId(){
        return this.id;
    }

    @Transient
    private Product product;

    @Transient
    private Order order;

    @Column("order_id")
    private Long orderId;

    public Long getOrderId(){
        return this.orderId;
    }

    @Column("product_id")
    private Long productId;

    public Long getProductId(){
        return this.productId;
    }

    @Column("quantity")
    private Long quantity;

    public ProductOrder(){}

    public ProductOrder(Long orderId, Long productId, Long quantity){
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public void setProduct(Product product){
        this.product = product;
    }

    public Product getProduct(){
        return this.product;
    }

    public Order getOrder(){
        return this.order;
    }

    public void setQuantity(Long quantity){
        this.quantity = quantity;
    }

    public Long getQuantity(){
        return this.quantity;
    }
}
