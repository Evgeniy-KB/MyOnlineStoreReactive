package task7.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.*;
import task7.model.CartProduct;
import task7.model.Product;

@Configuration
public class RedisConfig {
    @Bean
    public ReactiveRedisTemplate<String, Product> productReactiveRedisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisSerializationContext<String, Product> serializationContext =
                RedisSerializationContext
                        .<String, Product>newSerializationContext(new StringRedisSerializer())
                        .value(new Jackson2JsonRedisSerializer<>(Product.class))
                        .build();
        return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
    }

    @Bean
    public ReactiveRedisTemplate<String, Long> productsCountReactiveRedisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisSerializationContext<String, Long> serializationContext =
                RedisSerializationContext
                        .<String, Long>newSerializationContext(new StringRedisSerializer())
                        .value(new Jackson2JsonRedisSerializer<>(Long.class))
                        .build();
        return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
    }

    @Bean
    public ReactiveRedisTemplate<String, CartProduct> cartReactiveRedisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisSerializationContext<String, CartProduct> serializationContext =
                RedisSerializationContext
                        .<String, CartProduct>newSerializationContext(new StringRedisSerializer())
                        .key(new StringRedisSerializer())
                        .hashKey(new Jackson2JsonRedisSerializer<>(Long.class))
                        .hashValue(new Jackson2JsonRedisSerializer<>(CartProduct.class))
                        .build();
        return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
    }

    @Bean
    public ReactiveRedisTemplate<String, Object> cartProductRedisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisSerializationContext<String, Object> serializationContext =
                RedisSerializationContext
                        .<String, Object>newSerializationContext(new StringRedisSerializer())
                        .key(new StringRedisSerializer())
                        .hashKey(new Jackson2JsonRedisSerializer<>(Long.class))
                        .hashValue(new Jackson2JsonRedisSerializer<>(CartProduct.class))
                        .build();
        return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
    }
}
