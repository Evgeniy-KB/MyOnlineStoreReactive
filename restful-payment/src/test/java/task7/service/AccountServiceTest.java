package task7.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import task7.exception.InsufficientBalanceException;
import task7.model.Account;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableAutoConfiguration
public class AccountServiceTest {
    @Autowired
    private AccountServiceImpl accountServiceImpl;

    @Test
    @DirtiesContext
    public void testGetBalance() {
        Account account = new Account();
        Long balance = 105L;
        account.setBalance(balance);

        Long currentBalance = accountServiceImpl.getBalance().block();
        assertEquals(currentBalance, account.getBalance(), "Неверно определен баланс!");
    }

    @Test
    @DirtiesContext
    public void testSuccessfulPayOrderAndCheckBalance(){
        Account account = new Account();
        Long balance = 105L;
        account.setBalance(balance);

        Long totalPrice = 85L;//Первая цена покупки. Цена меньше баланса

        Boolean isSuccessfulBuy = accountServiceImpl.payOrder(totalPrice).block();
        assertTrue(isSuccessfulBuy, "Оплата заказа завершилась с ошибкой!");

        Long newBalance = accountServiceImpl.getBalance().block();
        assertEquals(newBalance, balance-totalPrice, "Неверно определен баланс после покупки!");

    }

    @Test
    @DirtiesContext
    public void testFailPayOrder(){
        Account account = new Account();
        Long balance = 105L;
        account.setBalance(balance);

        Long totalPrice = 110L;//Первая цена покупки. Цена больше баланса

        InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class, () ->{
            accountServiceImpl.payOrder(totalPrice).block();
        });

        assertEquals("Недостаточно средств для покупки", exception.getMessage(), "Оплата покупки невозможна из-за недостатка средств!");
    }

}
