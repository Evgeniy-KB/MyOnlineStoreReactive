package task6.MyOnlineStoreReactive.DTOTranslator;

import org.springframework.stereotype.Service;
import task6.MyOnlineStoreReactive.DTO.ProductDTO;
import task6.MyOnlineStoreReactive.model.CartProduct;
import task6.MyOnlineStoreReactive.model.Product;
import task6.MyOnlineStoreReactive.model.ProductOrder;

@Service
public class ProductDTOTranslator {
    public ProductDTO ToProductDTO(CartProduct cartProduct){
        Product product = cartProduct.getProduct();
        return new ProductDTO(product.getId(),
                product.getTitle(),
                product.getPicture(),
                product.getDescription(),
                cartProduct.getQuantity(),
                product.getPrice());
    }

    public ProductDTO ToProductDTO(ProductOrder productOrder){
        Product product = productOrder.getProduct();
        return new ProductDTO(
                product.getId(),
                product.getTitle(),
                product.getPicture(),
                product.getDescription(),
                productOrder.getQuantity(),
                product.getPrice());
    }

    public Product ToProduct(ProductDTO productDTO){
        return new Product(
                productDTO.getId(),
                productDTO.getTitle(),
                productDTO.getPicture(),
                productDTO.getDescription(),
                productDTO.getPrice());
    }

    public ProductDTO ToProductDTO(Product product){
        return new ProductDTO(
                product.getId(),
                product.getTitle(),
                product.getPicture(),
                product.getDescription(),
                0,
                product.getPrice());
    }

}
