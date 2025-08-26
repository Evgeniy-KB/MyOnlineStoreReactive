package task7.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import task7.dto.ProductDto;
import task7.repository.ProductRepository;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class ProductServiceRedisTest {

    @Autowired
    private ProductServiceImpl productServiceImpl;

    @MockitoSpyBean
    @Autowired
    private ProductRepository productRepository;

    @Test
    @DirtiesContext
    public void testCachingProductGetById(){
        Long productId = 3L;

        // извлекаем 2 раза один и тот же товар
        ProductDto notCachedProduct = productServiceImpl.getById(productId).block();
        ProductDto cachedProduct = productServiceImpl.getById(productId).block();

        // товар кэшируется при первом запросе и извлекаются из БД только 1 раз
        //verify(productRepository, times(1)).findById(productId);

        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    @DirtiesContext
    void testCachingGetProducts() {
        productServiceImpl.findAllWithPaginationByFilter("", 1L, 50L, "").blockLast();
        productServiceImpl.findAllWithPaginationByFilter("", 1L, 50L, "").blockLast();

        // данные закэшированы при запросе извлекаются из БД только 1 раз
        verify(productRepository, times(1)).findAllByFilter("");
    }
}


