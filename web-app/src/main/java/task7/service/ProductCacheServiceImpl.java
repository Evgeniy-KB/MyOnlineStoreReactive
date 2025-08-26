package task7.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import task7.model.Product;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;

@Service
public class ProductCacheServiceImpl implements ProductCacheService {
    @Autowired
    private final ReactiveRedisTemplate<String, Product> productReactiveRedisTemplate;

    @Autowired
    private final ReactiveRedisTemplate<String, Long> productsCountReactiveRedisTemplate;

    @Value("${spring.cache.redis.time-to-live-seconds}")
    private Long redisTtlSeconds;  //время жизни кэша в секундах

    public final String productKey = "product_";
    public final String productsKey = "products_";
    public final String productsCountKey = "productsCount_";

    @Autowired
    public ProductCacheServiceImpl(ReactiveRedisTemplate<String, Product> productReactiveRedisTemplate, ReactiveRedisTemplate<String, Long> productsCountReactiveRedisTemplate){
        this.productReactiveRedisTemplate = productReactiveRedisTemplate;
        this.productsCountReactiveRedisTemplate = productsCountReactiveRedisTemplate;
    }

    @Override
    public Mono<Boolean> hasCacheProductKey(Long id){
        String key = productKey + id;
        return productReactiveRedisTemplate.hasKey(key);
    }

    @Override
    public Mono<Product> setCacheProduct(Product product) {
        String key = productKey + product.getId();
        String productsPatternKeys = productsKey + "*";
        String productsCountPatternKeys = productsCountKey + "*";
        return productReactiveRedisTemplate.opsForValue().set(key, product, Duration.ofSeconds(redisTtlSeconds))
                //Очистим кэши, на которые влияют новые добавленные товары
                //.then(productReactiveRedisTemplate.delete(productReactiveRedisTemplate.keys("product_" + "*")))   //Кэши других товаров принудительно очищать не будем
                .then(productReactiveRedisTemplate.delete(productReactiveRedisTemplate.keys(productsPatternKeys)))
                .then(productsCountReactiveRedisTemplate.delete(productsCountReactiveRedisTemplate.keys(productsCountPatternKeys)))
                .map(x -> product);
    }

    @Override
    public Mono<Product> getCacheProductById(Long id){
        String key = productKey + id;
        return productReactiveRedisTemplate.opsForValue().get(key);
    }

    @Override
    public Mono<Long> getCacheProductsCount(String sample){
        String key = productsCountKey + sample;
        return productsCountReactiveRedisTemplate.opsForValue().get(key);
    }

    @Override
    public Mono<Boolean> setCacheProductsCount(String sample, Long countProducts){
        String key = productsCountKey + sample;
        return productsCountReactiveRedisTemplate.opsForValue().set(key, countProducts, Duration.ofSeconds(redisTtlSeconds));
    }

    public Comparator<Product> idComparator = (product1, product2) -> product1.getId().compareTo(product2.getId());

    @Override
    public Mono<Boolean> hasCacheProductsKey(String sample){
        String key = productsKey + sample;
        return productReactiveRedisTemplate.hasKey(key);
    }

    @Override
    public Mono<List<Product>> getCacheProducts(String sample){
        String key = productsKey + sample;
        return productReactiveRedisTemplate.opsForList().range(key, 0, -1).collectList()
                .map(x -> x.stream().sorted(idComparator).toList());
    }

    @Override
    public Mono<Boolean> setCacheProducts(String sample, List<Product> products){
        String key = productsKey + sample;
        return productReactiveRedisTemplate.opsForList().leftPushAll(key, products)
                .then(productReactiveRedisTemplate.expire(key, Duration.ofSeconds(redisTtlSeconds)));
    }

}
