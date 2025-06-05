package task6.MyOnlineStoreReactive.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import task6.MyOnlineStoreReactive.model.Cart;
import task6.MyOnlineStoreReactive.model.CartProduct;
import task6.MyOnlineStoreReactive.model.Product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@DataR2dbcTest
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartProductRepository cartProductRepository;

    @Test
    @DirtiesContext
    public void testExpectSavedAndFoundProducts() throws Exception{
        assertThat(productRepository.findAll().toIterable())
                .withFailMessage("Интернет-магазин не содержит товаров")
                .isNotEmpty()
                .withFailMessage("Интернет-магазин количество товаров, отличное от 6")
                .hasSize(6)
                .withFailMessage("Товар с ценой 100 должен присутствовать в интернет-магазине")
                .filteredOn(x -> x.getPrice() == 100)
                .first()
                .withFailMessage("Описание товара должно утверждать, что этот товар - «Самый дорогой товар»")
                .extracting(Product::getDescription)
                .isEqualTo("Самый дорогой товар");

        Product newProduct7 = new Product();
        newProduct7.setTitle("Товар7");
        newProduct7.setDescription("Редкий товар 7");//редкий товар(1)!
        newProduct7.setPrice(16L);

        Product newProduct8 = new Product();
        newProduct8.setTitle("Товар8");
        newProduct8.setDescription("Обычный товар 8");
        newProduct8.setPrice(7L);

        Product newProduct9 = new Product();
        newProduct9.setTitle("Товар9");
        newProduct9.setDescription("Редкий товар 9");//редкий товар(2)!
        newProduct9.setPrice(32L);

        productRepository.saveAll(List.of(newProduct7, newProduct8, newProduct9))
                .blockLast();

        assertThat(productRepository.findAll().toIterable())
                .withFailMessage("Количество товаров в интернет-магазине было увеличено")
                .hasSize(9);

        productRepository.findById(3L)//редкий товар(3)!
                .doOnNext(product -> assertThat(product)
                        .isNotNull()
                        .withFailMessage("С указанным индексом должен быть Редкий товар3")
                        .extracting(Product::getTitle)
                        .isEqualTo("Редкий Товар3"))
                .block();

        productRepository.findCountByFilter("реДкий")
                .doOnNext(x -> assertThat(x)
                        .withFailMessage("Не найдено товаров по поиску с образцом «реДкий»")
                        .isNotNull()
                        .withFailMessage("Количество редких товаров рассчитано неверно!")
                        .isEqualTo(3))
                .block();
    }

    @Test
    @DirtiesContext
    public void testExpectAddedAndIncrementedProductsInCart() throws Exception{
        Product newProduct7 = new Product(null, "Товар7", null, "Редкий товар 7", 16L);
        Product newProduct8 = new Product(null, "Товар8", null, "Обычный товар 8", 7L);
        Product newProduct9 = new Product(null, "Товар9", null, "Редкий товар 9", 32L);

        Cart cart = cartRepository.save(new Cart()).block();

        productRepository.saveAll(List.of(newProduct7, newProduct8, newProduct9))
                .flatMap(product -> {
                    Long quantity = 1L; //Товар 8 (цена 7) в количестве 1 штуки (общая цена - 7)
                    Long productId = product.getId();
                    if (productId == 7L)
                        quantity = 2L;  //Товар 7 (цена 16) в количестве 2 штук (общая цена - 32)
                    if (productId == 9L)
                        quantity = 3L;  //Товар 9 (цена 32) в количестве 3 штуки (общая цена - 96)
                    return cartProductRepository.save(new CartProduct(cart.getId(), product.getId(), quantity));
                })
                .blockLast();

        var cartProducts = cartRepository.findCurrentCart()
                .flatMap(c -> cartRepository.findCartProductsByCartId(c.getId())
                        .flatMap(cartProduct -> productRepository.findById(cartProduct.getProductId())
                                .map(product -> {
                                    cartProduct.setProduct(product);
                                    return cartProduct;
                                }))
                        .collectList())
                .block();

        assertThat(cartProducts)
                .withFailMessage("Неверное количество уникальных товаров в корзине товаров")
                .hasSize(3);

        assertThat(cartProducts
                .stream().mapToLong(x -> x.getQuantity() * x.getProduct().getPrice()).sum())
                .withFailMessage("Общая стоимость товаров в корзине не совпадает")
                .isEqualTo(135);

        //Увеличим количество товаров в корзине и еще раз проверим их общую стоимость в корзине
        cartRepository.updateQuantityProductInCart(cart.getId(), 7L, +5L).block();

        var cartProductsTotalPriceAfterUpdate = cartRepository.findCurrentCart()
                .flatMap(c -> cartRepository.findCartProductsByCartId(c.getId())
                        .flatMap(cartProduct -> productRepository.findById(cartProduct.getProductId())
                                .map(product -> {
                                    cartProduct.setProduct(product);
                                    return cartProduct;
                                }))
                        .collectList())
                .block()
                .stream().mapToLong(x -> x.getQuantity() * x.getProduct().getPrice()).sum();

        assertThat(cartProductsTotalPriceAfterUpdate)
                .withFailMessage("Общая стоимость товаров в корзине после увеличения не совпадает")
                .isEqualTo(215);

        //Удаляем корзину и проверяем ее наличие
        cartRepository.findCurrentCart()
                .flatMap(z -> cartRepository.deleteById(z.getId())
                        .then(cartRepository.existsById(z.getId())))
                .doOnNext(exists -> assertThat(exists)
                        .withFailMessage("Корзина для товаров должна быть удалена")
                        .isFalse())
                .block();
    }
}
