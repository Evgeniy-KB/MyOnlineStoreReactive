package task7.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "accounts")
public class Account {
    @Id
    private Long id;

    public Long getId() {
        return this.id;
    }

    @Column("balance")
    private Long balance;

    public Long getBalance() {
        return this.balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Account(){
    }
}

