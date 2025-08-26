package task7.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@Table(name="orders")
public class Order {
    @Id
    private Long id;

    @Transient
    private List<ProductOrder> productOrders = new ArrayList<>();

    public Order(){}

    public Long getId(){
        return this.id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public List<ProductOrder> getProductOrders(){
        return this.productOrders;
    }

    @Transient
    private List<Product> products;

    public Order orderWithProducts(List<ProductOrder> productOrders){
        this.productOrders = productOrders;
        return this;
    }
}
