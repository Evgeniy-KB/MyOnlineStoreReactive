package task6.MyOnlineStoreReactive.DTO;

import java.util.ArrayList;
import java.util.List;

public class CartDTO {
    private Long id;
    private List<ProductDTO> products = new ArrayList<>();

    public CartDTO(){}

    public CartDTO(Long id, List<ProductDTO> products){
        this.id = id;
        this.products = products;
    }

    public Long getId(){
        return this.id;
    }

    public List<ProductDTO> getProducts(){
        return this.products;
    }

    public int getTotalPrice(){
        return products.stream().mapToInt(x -> x.getQuantity() * x.getPrice()).sum();
    }
}
