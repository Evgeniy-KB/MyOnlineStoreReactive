package task6.MyOnlineStoreReactive.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import task6.MyOnlineStoreReactive.DTO.ProductDTO;
import task6.MyOnlineStoreReactive.service.CartServiceImpl;
import task6.MyOnlineStoreReactive.service.ProductServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest(ProductController.class)
public class ProductControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private ProductServiceImpl productServiceImpl;
    @MockitoBean
    private CartServiceImpl cartServiceImpl;

    @Test
    public void testGetProductsAndShouldReturnHtml() throws Exception{
        ProductDTO testProduct1 = new ProductDTO(1L, "Товар 1", null, "Описание товара 1",125L);
        ProductDTO testProduct2 = new ProductDTO(2L, "Товар 2", null, "Описание товара 2",37L);

        Flux<ProductDTO> productDTOFlux = Flux.just(testProduct1, testProduct2);

        Mockito.when(productServiceImpl.findAllWithPaginationByFilter("", 1L, 50L, "")).thenReturn(productDTOFlux);
        Mockito.when(productServiceImpl.findCountByFilter("")).thenReturn(Mono.just(2L));

        webTestClient.get()
                .uri("/products")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.TEXT_HTML)
                .expectBody()
                .xpath("//table/tbody/tr").nodeCount(2)
                .xpath("//table/tbody/tr[1]/td[3]").isEqualTo("Описание товара 1")
                .xpath("//table/tbody/tr[2]/td[4]").isEqualTo("37");
    }

    @Test
    public void addNewProductToDatabaseAndRedirect() throws Exception{
        ProductDTO newProductDTO = new ProductDTO(null, "Товар 7", null, "Описание товара 7",77L);
        ProductDTO savedProductDTO = new ProductDTO(1L, "Товар 7", null, "Описание товара 7",77L);

        Mockito.when(productServiceImpl.save(Mockito.any(ProductDTO.class)))
                .thenReturn(Mono.just(savedProductDTO));

        webTestClient.post()
                .uri("/products/save")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(newProductDTO), ProductDTO.class)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("Location", "/products");

        Mockito.verify(productServiceImpl, Mockito.times(1))
                .save(Mockito.any(ProductDTO.class));
    }

    @Test
    public void testGetProductAndShouldReturnHtml() throws Exception {
        ProductDTO testProduct1 = new ProductDTO(1L, "Товар 1", null, "Описание товара 1", 125L);

        Mockito.when(productServiceImpl.getById(1L)).thenReturn(Mono.just(testProduct1));

        webTestClient.get()
                .uri("/products/{id}", 1L)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.TEXT_HTML)
                .expectBody(String.class).consumeWith(response -> {
                    var body = response.getResponseBody();
                    assertNotNull(body);
                    assertTrue(body.contains(testProduct1.getTitle()));
                    assertTrue(body.contains(testProduct1.getDescription()));
                    assertTrue(body.contains(""+testProduct1.getPrice()));
                });
    }
}


