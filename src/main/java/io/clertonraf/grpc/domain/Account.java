package io.clertonraf.grpc.domain;


import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="account")
public class Account {

    @Id
    private int user;

    @Version
    private int version;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "walletPK.account")
    private Set<Wallet> wallets;

    public Set<Wallet> getWallets() {
        return wallets;
    }

    public void setWallets(Set<Wallet> wallets) {
        this.wallets = wallets;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

}
