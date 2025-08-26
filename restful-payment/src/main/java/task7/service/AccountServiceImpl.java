package task7.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import task7.exception.InsufficientBalanceException;
import task7.repository.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService{

    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    @Override
    public Mono<Long> getBalance() {
        return accountRepository.findCurrentAccount()
                .flatMap(account -> accountRepository.getBalance(account.getId()));
    }

    @Override
    public Mono<Boolean> payOrder(Long price) {
        return accountRepository.findCurrentAccount()
                .flatMap(account -> {
                    //Long newBalance = account.getBalance() - price - 50;
                    Long newBalance = account.getBalance() - price;

                    if (newBalance >= 0){
                        account.setBalance(newBalance);
                        return accountRepository.save(account).then(Mono.just(true));
                    }

                    return Mono.error(new InsufficientBalanceException("Недостаточно средств для покупки"));
                });
    }

}
