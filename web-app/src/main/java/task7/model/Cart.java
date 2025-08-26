package task7.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@Table(name = "carts")
public class Cart {
    @Id
    private Long id;

    @Transient
    private List<CartProduct> cartProducts = new ArrayList<>();


    public Cart(){}

    public Cart(Long id){
        this.id = id;
    }

    public Long getId(){
        return this.id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public List<CartProduct> getCartProducts(){
        return this.cartProducts;
    }

    public Cart cartWithProducts(List<CartProduct> cartProducts){
        this.cartProducts = cartProducts;
        return this;
    }

    @Transient
    private List<Product> products;

    public List<Product> getProducts(){
        return this.cartProducts.stream().map(x -> x.getProduct()).toList();
    }
}
