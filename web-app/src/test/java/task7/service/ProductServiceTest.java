package task7.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import task7.dto.ProductDto;

@SpringBootTest
@EnableAutoConfiguration
public class ProductServiceTest extends AbstractTestContainerTest{
    @Autowired
    private ProductServiceImpl productServiceImpl;

    @Test
    @DirtiesContext
    public void testSaveProductAndCheckDescription(){
        ProductDto newProduct = new ProductDto(null, "Тестовый товар 7", null, "Описание тестового товара 7", 99L);

        productServiceImpl.save(newProduct)
                .doOnNext(productDto -> org.assertj.core.api.Assertions.assertThat(productDto)
                        .withFailMessage("Результат сохранения не должен быть пустым")
                        .isNotNull()
                        .withFailMessage("Описание товара должно совпадать с ожидаемым значением")
                        .extracting(ProductDto::getDescription)
                        .isEqualTo("Описание тестового товара 7")
                ).block();
    }
}
