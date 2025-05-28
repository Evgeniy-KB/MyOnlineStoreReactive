package task6.MyOnlineStoreReactive.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import task6.MyOnlineStoreReactive.model.Cart;
import task6.MyOnlineStoreReactive.model.Product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

/*@ActiveProfiles("test")
@DataJpaTest
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartRepository cartRepository;

    @Test
    @DirtiesContext
    public void expectSavedAndFoundProducts() throws Exception{
        assertThat(productRepository.findAll()).hasSize(6);

        Product newProduct7 = new Product();
        newProduct7.setTitle("Товар7");
        newProduct7.setDescription("Редкий товар 7");
        newProduct7.setPrice(16);
        productRepository.save(newProduct7);

        Product newProduct8 = new Product();
        newProduct8.setTitle("Товар8");
        newProduct8.setDescription("Обычный товар 8");
        newProduct8.setPrice(7);
        productRepository.save(newProduct8);

        Product newProduct9 = new Product();
        newProduct9.setTitle("Товар9");
        newProduct9.setDescription("Редкий товар 9");
        newProduct9.setPrice(32);
        productRepository.save(newProduct9);

        assertThat(productRepository.findAll()).hasSize(9);

        Product rareProduct = productRepository.getById(3L);
        assertEquals(rareProduct.getDescription(), "Довольно редкий товар 3");

        int countRaraProducts = productRepository.findCountByFilter("реДкий");
        assertEquals(3, countRaraProducts);
    }

    @Test
    @DirtiesContext
    public void expectAddedAndIncrementedProductsInCart() throws Exception{
        Product newProduct7 = new Product();
        newProduct7.setTitle("Товар7");
        newProduct7.setDescription("Редкий товар 7");
        newProduct7.setPrice(16);
        productRepository.save(newProduct7);

        Product newProduct8 = new Product();
        newProduct8.setTitle("Товар8");
        newProduct8.setDescription("Обычный товар 8");
        newProduct8.setPrice(7);
        productRepository.save(newProduct8);

        Product newProduct9 = new Product();
        newProduct9.setTitle("Товар9");
        newProduct9.setDescription("Редкий товар 9");
        newProduct9.setPrice(32);
        productRepository.save(newProduct9);

        Cart cart = new Cart();
        cart.addProductQuantity(newProduct7, 2);
        cart.addProductQuantity(newProduct8, 1);
        cart.addProductQuantity(newProduct9, 3);
        cartRepository.save(cart);

        //После добавления товаров в корзину проверяем наличие корзины
        Optional<Cart> findCart = cartRepository.findCurrentCart();
        assertThat(findCart.isPresent()).isNotNull();

        //Проверяем общую стоимость товаров в корзине
        assertEquals(135, findCart.get().getCartProducts().stream().mapToInt(x -> x.getQuantity() * x.getProduct().getPrice()).sum());

        //Увеличим количество товаров в корзине и еще раз проверим их общую стоимость в корзине
        cartRepository.updateQuantityProductInCart(findCart.get().getId(), 7L, +5L);
        findCart = cartRepository.findCurrentCart();
        assertEquals(215, findCart.get().getCartProducts().stream().mapToInt(x -> x.getQuantity() * x.getProduct().getPrice()).sum());

        //Удяляем корзину и проверяем ее наличие
        cartRepository.deleteById(findCart.get().getId());
        Optional<Cart> notExistsCart = cartRepository.findCurrentCart();
        assertThat(notExistsCart.isPresent()).isFalse();
    }
}*/
