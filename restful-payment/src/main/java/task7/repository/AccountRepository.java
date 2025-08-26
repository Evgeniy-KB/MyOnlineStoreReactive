package task7.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import task7.model.Account;

@Repository
public interface AccountRepository extends R2dbcRepository<Account, Long> {
    @Query("SELECT a.* FROM accounts a LIMIT 1")
    Mono<Account> findCurrentAccount();

    @Query("SELECT a.balance FROM accounts a where a.id = :account_id")
    Mono<Long> getBalance(@Param("account_id") Long account_id);
}
