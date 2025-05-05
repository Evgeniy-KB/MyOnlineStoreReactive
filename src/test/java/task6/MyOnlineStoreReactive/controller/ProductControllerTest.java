package task6.MyOnlineStoreReactive.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import task6.MyOnlineStoreReactive.DTO.ProductDTO;
import task6.MyOnlineStoreReactive.service.CartService;
import task6.MyOnlineStoreReactive.service.ProductService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@ActiveProfiles("test")
@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ProductService productService;
    @MockitoBean
    private CartService cartService;

    @Test
    @DirtiesContext
    public void getProductsAndShouldReturnHtml() throws Exception{
        ProductDTO testProduct1 = new ProductDTO(1L, "Товар 1", null, "Описание товара 1",125);
        ProductDTO testProduct2 = new ProductDTO(2L, "Товар 2", null, "Описание товара 2",37);

        when(productService.findAllWithPaginationByFilter("", 1, 50, "")).thenReturn(List.of(testProduct1, testProduct2));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("products"))
                .andExpect(model().attributeExists("products"))
                .andExpect(xpath("//table/tbody/tr").nodeCount(2))
                .andExpect(xpath("//table/tbody/tr[1]/td[3]").string("Описание товара 1"));
    }

    @Test
    @DirtiesContext
    public void addNewProductToDatabaseAndRedirect() throws Exception{
        mockMvc.perform(post("/products/save")
                        .param("title", "Товар 7")
                        .param("picture", "")
                        .param("description", "Описание товара7")
                        .param("price", "77"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));
    }
}


