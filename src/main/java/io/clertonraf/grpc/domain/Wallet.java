package io.clertonraf.grpc.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="wallet")
public class Wallet {

    @Id
    private String user;

    @Version
    private int version;

    private BigDecimal balance;

    @Column(name="currency")
    private String defaultCurrency;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(String defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }
}
