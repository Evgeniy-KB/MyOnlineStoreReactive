package task6.MyOnlineStoreReactive.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import task6.MyOnlineStoreReactive.DTO.ProductDTO;
import task6.MyOnlineStoreReactive.DTOTranslator.ProductDTOTranslator;
import task6.MyOnlineStoreReactive.model.Product;
import task6.MyOnlineStoreReactive.repository.ProductRepository;

import java.util.Comparator;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final ProductDTOTranslator productDTOTranslator;
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductDTOTranslator productDTOTranslator){
        this.productRepository = productRepository;
        this.productDTOTranslator = productDTOTranslator;
    }

    @Override
    public Mono<ProductDTO> getById(Long id){
        return productRepository.findById(id).map(productDTOTranslator::ToProductDTO);
    }

    @Override
    @Transactional
    public Mono<Long> save(ProductDTO productDTO) {
        Product product = productDTOTranslator.ToProduct(productDTO);
        return productRepository.save(product).map(Product::getId);
    }

    @Override
    public Mono<Long> findCountByFilter(String sampleSearch){
        return productRepository.findCountByFilter(sampleSearch);
    }

    @Override
    public Flux<ProductDTO> findAllWithPaginationByFilter(String sampleSearch, Long pageNumber, Long pageSize, String sorting){
        Flux<Product> products = productRepository.findAllByFilter(sampleSearch);

        Comparator<Product> titleComparator = (product1,product2) -> product1.getTitle().compareTo(product2.getTitle());
        Comparator<Product> priceComparator = (product1,product2) -> product1.getPrice() - product2.getPrice();

        if (sorting.equals("title"))
            products = products.sort(titleComparator);
        else if (sorting.equals("price"))
            products = products.sort(priceComparator);

        return products
                .skip((pageNumber-1)*pageSize)
                .take(pageSize)
                .map(productDTOTranslator::ToProductDTO);
    }
}


