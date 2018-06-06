package io.clertonraf.grpc.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="wallet")
public class Wallet {

    @Version
    private int version;

    @EmbeddedId
    private WalletPK walletPK;

    @Column(nullable = false)
    private BigDecimal balance;

    public WalletPK getWalletPK() {
        return walletPK;
    }

    public void setWalletPK(WalletPK walletPK) {
        this.walletPK = walletPK;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

}
