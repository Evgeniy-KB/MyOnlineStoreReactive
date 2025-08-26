package task7.service;

import reactor.core.publisher.Mono;
import task7.model.Product;

import java.util.List;

public interface ProductCacheService {
    Mono<Boolean> hasCacheProductKey(Long id);

    Mono<Product> getCacheProductById(Long id);

    Mono<Product> setCacheProduct(Product product);

    Mono<Long> getCacheProductsCount(String sample);

    Mono<Boolean> setCacheProductsCount(String sample, Long productsCount);

    Mono<Boolean> hasCacheProductsKey(String sample);

    Mono<List<Product>> getCacheProducts(String sample);

    Mono<Boolean> setCacheProducts(String sample, List<Product> products);

}

