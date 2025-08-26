package task7.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import task7.dto.CartDto;
import task7.dto.ProductDto;

public class ProductServiceTestContainers extends AbstractTestContainerTest {
    @Autowired
    private ProductServiceImpl productServiceImpl;

    @Autowired
    private CartServiceImpl cartServiceImpl;

    @Test
    @DisplayName("Проверка работы корзины товаров интернет-магазина")
    void testAddProductsInCartAndCalcTotalPrice(){
        ProductDto product1 = new ProductDto(null, "Тестовый товар 1", null, "Описание тестового товара 1", 99L);
        ProductDto product2 = new ProductDto(null, "Тестовый товар 2", null, "Описание тестового товара 2", 35L);
        ProductDto product3 = new ProductDto(null, "Тестовый товар 3", null, "Описание тестового товара 3", 10L);

        cartServiceImpl.findCurrentCart()
                .doOnNext(cart -> org.assertj.core.api.Assertions.assertThat(cart)
                        .withFailMessage("Текущая корзина должна существовать")
                        .isNotNull()
                        .withFailMessage("Товары в корзине отсутствуют. Стоимость корзины должна быть 0")
                        .extracting(CartDto::getTotalPrice)
                        .isEqualTo(0L)
                ).block();

        //Сохраним 3 товара и добавим каждый товар в количестве двух штук в корзину
        Flux.just(product1, product2, product3)
                .flatMap(product -> productServiceImpl.save(product)
                        .flatMap(saved_product -> cartServiceImpl.addProductQuantity(saved_product.getId(), 2L)) )
                .blockLast();

        cartServiceImpl.findCurrentCart()
                .doOnNext(cart -> org.assertj.core.api.Assertions.assertThat(cart)
                        .withFailMessage("Текущая корзина должна существовать")
                        .isNotNull()
                        .withFailMessage("Итоговая цена товаров в корзине должна совпадать с ожидаемым значением")
                        .extracting(CartDto::getTotalPrice)
                        .isEqualTo(288L)
                ).block();
    }
}
