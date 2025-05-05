package task6.MyOnlineStoreReactive.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<ProductOrder> productOrders = new ArrayList<>();

    public Order(){}

    public Order(List<ProductOrder> productOrders){
        this.productOrders = productOrders;
    }

    public Long getId(){
        return this.id;
    }

    public List<ProductOrder> getProductOrders(){
        return this.productOrders;
    }

    /*public void addProductOrder(ProductOrder productOrder){
        this.productOrders.add(productOrder);
    }*/

    public void addProductQuantity(Product product, int quantity){
        this.productOrders.add(new ProductOrder(this, product, quantity));
    }


}
