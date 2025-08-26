package task7.mapper;

import org.springframework.stereotype.Service;
import task7.dto.ProductDto;
import task7.model.CartProduct;
import task7.model.Product;
import task7.model.ProductOrder;
import task7.repository.ProductRepository;

@Service
public class ProductMapper {

    ProductRepository productRepository;

    public ProductMapper(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public ProductDto ToProductDto(CartProduct cartProduct){
        Product product = cartProduct.getProduct();
        return new ProductDto(product.getId(),
                product.getTitle(),
                product.getPicture(),
                product.getDescription(),
                cartProduct.getQuantity(),
                product.getPrice());
    }

    public ProductDto ToProductDto(ProductOrder productOrder){
        Product product = productOrder.getProduct();
        return new ProductDto(
                product.getId(),
                product.getTitle(),
                product.getPicture(),
                product.getDescription(),
                productOrder.getQuantity(),
                product.getPrice());
    }

    public Product ToProduct(ProductDto productDto){
        return new Product(
                productDto.getId(),
                productDto.getTitle(),
                productDto.getPicture(),
                productDto.getDescription(),
                productDto.getPrice());
    }

    public ProductDto ToProductDto(Product product){
        return new ProductDto(
                product.getId(),
                product.getTitle(),
                product.getPicture(),
                product.getDescription(),
                0L,
                product.getPrice());
    }

}
