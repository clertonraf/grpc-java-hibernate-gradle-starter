package io.clertonraf.grpc.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class WalletPK implements Serializable {


    @ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user", nullable = false)
    @MapsId("user")
    private Account account;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WalletPK)) return false;
        WalletPK that = (WalletPK) o;
        return Objects.equals(getAccount(), that.getAccount()) &&
                Objects.equals(getCurrency(), that.getCurrency()
                );
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAccount(), getCurrency());
    }

}
