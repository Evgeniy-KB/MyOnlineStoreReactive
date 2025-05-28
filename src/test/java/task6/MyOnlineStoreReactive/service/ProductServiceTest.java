package task6.MyOnlineStoreReactive.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import task6.MyOnlineStoreReactive.DTO.CartDTO;
import task6.MyOnlineStoreReactive.DTO.ProductDTO;
import task6.MyOnlineStoreReactive.DTOTranslator.CartDTOTranslator;
import task6.MyOnlineStoreReactive.DTOTranslator.ProductDTOTranslator;
import task6.MyOnlineStoreReactive.model.Cart;
import task6.MyOnlineStoreReactive.model.Product;
import task6.MyOnlineStoreReactive.repository.CartRepository;
import task6.MyOnlineStoreReactive.repository.OrderRepository;
import task6.MyOnlineStoreReactive.repository.ProductRepository;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/*@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableAutoConfiguration
public class ProductServiceTest {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductDTOTranslator productDTOTranslator;
    @Autowired
    private CartDTOTranslator cartDTOTranslator;

    @Autowired
    private ProductServiceImpl productService;
    @Autowired
    private CartServiceImpl cartService;

    @BeforeEach
    public void setUp() {
        productRepository = Mockito.mock(ProductRepository.class);
        productService = new ProductServiceImpl(productRepository, productDTOTranslator);

        orderRepository = Mockito.mock(OrderRepository.class);
        cartRepository = Mockito.mock(CartRepository.class);
        cartService = new CartServiceImpl(productRepository, orderRepository, cartRepository, cartDTOTranslator);
    }

    @Test
    public void saveAndGetProductById(){
        Product mockProduct = new Product(1L, "Тестовый товар 1", null, "Описание тестового товара 1", 99);

        Mockito.when(productRepository.getById(1L)).thenReturn(mockProduct);

        ProductDTO actualProduct = productService.getById(1L);

        assertEquals("Описание тестового товара 1", actualProduct.getDescription(), "Описание товара должно совпадать с ожидаемым значением");

        verify(productRepository, times(1)).getById(1L);
    }

    @Test
    public void addProductsInCartAndCalcTotalPrice(){
        Product mockProduct1 = new Product(1L, "Тестовый товар 1", null, "Описание тестового товара 1", 99);
        Product mockProduct2 = new Product(2L, "Тестовый товар 2", null, "Описание тестового товара 2", 35);
        Product mockProduct3 = new Product(3L, "Тестовый товар 3", null, "Описание тестового товара 3", 10);

        when(productRepository.getById(1L)).thenReturn(mockProduct1);
        when(productRepository.getById(2L)).thenReturn(mockProduct2);
        when(productRepository.getById(3L)).thenReturn(mockProduct3);

        Cart cart = new Cart(1L);

        when(cartRepository.findCurrentCart()).thenReturn(Optional.of(cart));

        cartService.addProductQuantity(1L, 2);  //вызов findCurrentCart() №1
        cartService.addProductQuantity(2L, 1);  //вызов findCurrentCart() №2
        cartService.addProductQuantity(3L, 3);  //вызов findCurrentCart() №3

        Optional<CartDTO> actualCart =  cartService.findCurrentCart();  //вызов findCurrentCart() №4
        assertThat(actualCart.isPresent()).isTrue();

        assertEquals(263, actualCart.get().getTotalPrice(), "Общая цена корзины должно совпадать с ожидаемым значением");

        verify(cartRepository, times(4)).findCurrentCart();
    }
}*/
