package io.clertonraf.grpc.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="wallet")
public class Wallet {

    @Id @GeneratedValue
    private int walletId;

    @ManyToOne(cascade = CascadeType.ALL)
    private Account account;

    @Version
    private int version;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(nullable = false)
    private BigDecimal balance;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
