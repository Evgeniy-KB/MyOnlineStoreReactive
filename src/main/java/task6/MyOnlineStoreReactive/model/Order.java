package task6.MyOnlineStoreReactive.model;

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

    /*public Order(List<ProductOrder> productOrders){
        this.productOrders = productOrders;
    }

    public Order(Long id){
        this.id = id;
    }*/

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

    /*public void addProductOrder(ProductOrder productOrder){
        this.productOrders.add(productOrder);
    }*/

    /*public void addProductQuantity(Product product, Long quantity){
        this.productOrders.add(new ProductOrder(this, product, quantity));
    }*/

    public Order orderWithProducts(List<ProductOrder> productOrders){
        this.productOrders = productOrders;
        return this;
    }



}
