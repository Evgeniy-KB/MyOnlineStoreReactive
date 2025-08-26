package task7.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import task7.model.Account;
import task7.server.model.PayResponse;
import task7.service.AccountServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureWebTestClient
public class PaymentApiControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private AccountServiceImpl accountServiceImpl;

    @Test
    public void testPaymentAndShouldReturnOkResponse(){
        Account account = new Account();
        Long balance = 105L;
        account.setBalance(balance);

        Long totalPrice = 100L;//Цена МЕНЬШЕ баланса

        Mockito.when(accountServiceImpl.payOrder(totalPrice)).thenReturn(Mono.just(balance >= totalPrice));

        webTestClient.patch()
                .uri(uriBuilder -> uriBuilder.path("/api/pay").queryParam("price", totalPrice).build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(new MediaType(MediaType.APPLICATION_JSON))
                .expectBody(PayResponse.class).consumeWith(response -> {
                    var body = response.getResponseBody();
                    assertNotNull(body);
                    assertEquals(body.getMessage(), "Заказ оформлен");
                });
    }

    @Test
    public void testPaymentAndShouldReturnDenyResponse(){
        Account account = new Account();
        Long balance = 105L;
        account.setBalance(balance);

        Long totalPrice = 110L;//Цена БОЛЬШЕ баланса

        Mockito.when(accountServiceImpl.payOrder(totalPrice)).thenReturn(Mono.just(balance >= totalPrice));

        webTestClient.patch()
                .uri(uriBuilder -> uriBuilder.path("/api/pay").queryParam("price", totalPrice).build())
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentType(new MediaType(MediaType.APPLICATION_JSON))
                .expectBody(PayResponse.class).consumeWith(response -> {
                    var body = response.getResponseBody();
                    assertNotNull(body);
                    assertEquals(body.getMessage(), "Недостаточно средств для покупки");
                });
    }
}


