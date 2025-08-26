package task7.dto;

import java.util.ArrayList;
import java.util.List;

public class OrderDto {
    private Long id;
    private List<ProductDto> products = new ArrayList<>();

    public OrderDto(){}

    public OrderDto(Long id, List<ProductDto> products){
        this.id = id;
        this.products = products;
    }

    public Long getId(){
        return this.id;
    }

    public List<ProductDto> getProducts(){
        return this.products;
    }

    public Long getTotalPrice(){
        return products.stream().mapToLong(x -> x.getQuantity() * x.getPrice()).sum();
    }
}
