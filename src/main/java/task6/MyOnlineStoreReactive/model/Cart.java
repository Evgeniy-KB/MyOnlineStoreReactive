package task6.MyOnlineStoreReactive.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartProduct> cartProducts = new ArrayList<>();

    public Cart(){}

    public Cart(Long id){
        this.id = id;
    }

    public Long getId(){
        return this.id;
    }

    public List<CartProduct> getCartProducts(){
        return this.cartProducts;
    }

    public List<Product> getProducts(){
        return this.getCartProducts().stream().map(x -> x.getProduct()).toList();
    }

    public void addProductQuantity(Product product, int quantity){
        Optional<CartProduct> cartProduct = this.cartProducts.stream()
                .filter(x -> x.getProduct().getId() == product.getId())
                .findFirst();

        if (cartProduct.isPresent()){
            cartProduct.get().addQuantity(quantity);
        }
        else{
            this.cartProducts.add(new CartProduct(this, product, quantity));
        }
    }

}
