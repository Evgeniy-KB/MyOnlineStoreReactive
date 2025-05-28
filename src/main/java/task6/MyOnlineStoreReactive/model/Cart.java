package task6.MyOnlineStoreReactive.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    /*public void addCartProduct(CartProduct cartProduct){
        //System.out.println(" cart.id " + " this addCartProduct " + cartProduct.getQuantity());
        this.cartProducts.add(cartProduct);
    }

    public void addCartProducts(List<CartProduct> cartProduct){
        //System.out.println(" cart.id " + this.getId() + " this addCartProduct " + cartProduct.getId());
        this.cartProducts.addAll(cartProduct);
    }*/


    public Cart cartWithProducts(List<CartProduct> cartProducts){
        this.cartProducts = cartProducts;
        return this;
    }

    /*public void updateProductQuantity(Long productId, Long quantity){
        Optional<CartProduct> cartProduct = this.cartProducts.stream().filter(x -> x.getProductId() == productId).findFirst();

        cartProduct.ifPresent(cp -> cp.setQuantity(cp.getQuantity() + quantity));
    }*/

    @Transient
    private List<Product> products;

    public List<Product> getProducts(){
        return this.cartProducts.stream().map(x -> x.getProduct()).toList();
    }

    /*@Transient
    public List<Product> getProducts(){
        return this.getCartProducts().stream().map(x -> x.getProduct()).toList();
    }*/

    /*public void addProductQuantity(Product product, Long quantity){
        System.out.println("1********************************************************************`");
        Optional<CartProduct> cartProduct = this.cartProducts.stream()
                .filter(x -> x.getProduct().getId() == product.getId())
                .findFirst();

        if (cartProduct.isPresent()){
            cartProduct.get().addQuantity(quantity);
        }
        else{
            System.out.println("2********************************************************************`");
            this.cartProducts.add(new CartProduct(this, product, quantity));
        }
    }*/

}
