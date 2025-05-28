package task6.MyOnlineStoreReactive.DTO;

import java.util.ArrayList;
import java.util.List;

public class OrderDTO {
    private Long id;
    private List<ProductDTO> products = new ArrayList<>();

    public OrderDTO(){}

    public OrderDTO(Long id, List<ProductDTO> products){
        this.id = id;
        this.products = products;
    }

    public Long getId(){
        return this.id;
    }

    public List<ProductDTO> getProducts(){
        return this.products;
    }

    public Long getTotalPrice(){
        return products.stream().mapToLong(x -> x.getQuantity() * x.getPrice()).sum();
    }
}
