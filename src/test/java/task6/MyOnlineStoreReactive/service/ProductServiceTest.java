package task6.MyOnlineStoreReactive.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import task6.MyOnlineStoreReactive.DTO.ProductDTO;

@SpringBootTest
@EnableAutoConfiguration
public class ProductServiceTest {
    @Autowired
    private ProductServiceImpl productServiceImpl;

    @Test
    @DirtiesContext
    public void testSaveProductAndCheckDescription(){
        ProductDTO newProduct = new ProductDTO(null, "Тестовый товар 7", null, "Описание тестового товара 7", 99L);

        productServiceImpl.save(newProduct)//.getById(1L)
                .doOnNext(productDTO -> org.assertj.core.api.Assertions.assertThat(productDTO)
                        .withFailMessage("Результат сохранения не должен быть пустым")
                        .isNotNull()
                        .withFailMessage("Описание товара должно совпадать с ожидаемым значением")
                        .extracting(ProductDTO::getDescription)
                        .isEqualTo("Описание тестового товара 7")
                ).block();
    }
}
