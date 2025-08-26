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
import task7.server.model.BalanceResponse;
import task7.service.AccountServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureWebTestClient
public class BalanceApiControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private AccountServiceImpl accountServiceImpl;

    @Test
    public void testGetBalanceAndShouldReturnResponse() throws Exception{
        Account account = new Account();
        Long balance = 105L;
        account.setBalance(balance);

        Mockito.when(accountServiceImpl.getBalance()).thenReturn(Mono.just(account.getBalance()));

        webTestClient.get()
                .uri("/api/balance")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BalanceResponse.class).consumeWith(response -> {
                    var body = response.getResponseBody();
                    assertNotNull(body);
                    assertEquals(body.getBalance(), account.getBalance());
                });
    }
}


