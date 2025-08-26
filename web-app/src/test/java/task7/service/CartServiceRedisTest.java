package task7.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import task7.dto.CartDto;
import task7.repository.*;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@EnableAutoConfiguration
public class CartServiceRedisTest{
    @Autowired
    private CartServiceImpl cartServiceImpl;

    @Autowired
    private CartCacheServiceImpl cartCacheServiceImpl;

    @MockitoSpyBean
    @Autowired
    private CartRepository cartRepository;

    @AfterEach
    public void tearDownDeleteCartAndHerCache(){
        // Очистим корзину и ее кэш
        CartDto cartDto = cartServiceImpl.findCurrentCart().block();
        cartServiceImpl.deleteCart(cartDto.getId()).block();
    }

    @Test
    @DirtiesContext
    void testCachingCurrentCartProductsAndDeleteCart() {
        // Добавим товары в корзину
        cartServiceImpl.addProductQuantity(1L, 2L).block();
        cartServiceImpl.addProductQuantity(4L, 5L).block();
        cartServiceImpl.addProductQuantity(3L, 1L).block();
        cartServiceImpl.addProductQuantity(4L, 2L).block();
        CartDto cartDto = cartServiceImpl.findCurrentCart().block();
        Long cartId = cartDto.getId();

        // Проверим существование корзины товаров в базе и кэше
        CartDto existsCart = cartServiceImpl.findCartById(cartId).block();
        assertNotNull(existsCart, "Корзина товаров не существует!");

        Boolean isCartExists = cartCacheServiceImpl.hasCacheCartByKey(cartId).block();
        assertTrue(isCartExists, "Корзина товаров в кэше не существует!");


        // Убедимся, что в кэше корзины товаров верное количество товаров
        var cartProducts = cartCacheServiceImpl.getCacheCartProducts(cartId).collectList().block();
        assertEquals(cartProducts.size(), 3L, "Неверное количество уникальных товаров в кэше корзины!");
        assertEquals(cartProducts.stream().map(cp -> cp.getQuantity()).reduce(0L, (x,y) -> x + y), 10L, "Неверное общее количество товаров в кэше корзины!");


        //Удаляем корзину и проверяем ее отсутствие в базе и кэше
        cartServiceImpl.deleteCart(cartId).block();

        existsCart = cartServiceImpl.findCartById(cartId).block();
        assertNull(existsCart, "Корзина товаров существует!");

        isCartExists = cartCacheServiceImpl.hasCacheCartByKey(cartId).block();
        assertFalse(isCartExists, "Корзина товаров в кэше существует!");
    }
}


