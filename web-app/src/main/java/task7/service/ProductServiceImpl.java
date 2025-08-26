package task7.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import task7.dto.ProductDto;
import task7.mapper.ProductMapper;
import task7.model.Product;
import task7.repository.ProductRepository;

import java.util.Comparator;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final ProductMapper productMapper;
    @Autowired
    private final ProductCacheService productCacheService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper, ProductCacheService productCacheService){
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productCacheService = productCacheService;
    }

    @Override
    public Mono<ProductDto> getById(Long id){
        return productCacheService.hasCacheProductKey(id)
                .flatMap(hasKey -> {
                    if (hasKey){
                        return productCacheService.getCacheProductById(id);
                    }
                    else return productRepository.findById(id)
                            .flatMap(product -> productCacheService.setCacheProduct(product).map(x -> product));
                }).map(productMapper::ToProductDto);
    }

    @Override
    @Transactional
    public Mono<ProductDto> save(ProductDto productDto){
        Product product = productMapper.ToProduct(productDto);
        return productRepository.save(product)
                .flatMap(p -> productCacheService.setCacheProduct(p)
                        .map(productMapper::ToProductDto));
    }

    @Override
    public Mono<Long> findCountByFilter(String sampleSearch){
        return productCacheService.getCacheProductsCount(sampleSearch)
                .switchIfEmpty(productRepository.findCountByFilter(sampleSearch)
                        .flatMap(countProducts -> productCacheService.setCacheProductsCount(sampleSearch, countProducts)
                                .map(x -> countProducts)));
    }

    @Override
    public Flux<ProductDto> findAllWithPaginationByFilter(String sampleSearch, Long pageNumber, Long pageSize, String sorting){
        var products = productCacheService.hasCacheProductsKey(sampleSearch)
                .flatMap(hasKey ->{
                    if (hasKey) {
                        return productCacheService.getCacheProducts(sampleSearch);
                    }
                    else {
                        return productRepository.findAllByFilter(sampleSearch).collectList()
                                .flatMap(listProducts -> productCacheService.setCacheProducts(sampleSearch, listProducts)
                                        .map(x -> listProducts));
                    }
                }).flatMapMany(Flux::fromIterable);

        Comparator<Product> titleComparator = (product1,product2) -> product1.getTitle().compareTo(product2.getTitle());
        Comparator<Product> priceComparator = (product1,product2) -> Long.compare(product1.getPrice(), product2.getPrice());

        if (sorting.equals("title"))
            products = products.sort(titleComparator);
        else if (sorting.equals("price"))
            products = products.sort(priceComparator);

        return products
                .skip((pageNumber-1)*pageSize)
                .take(pageSize)
                .map(productMapper::ToProductDto);
    }
}


