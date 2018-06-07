package io.clertonraf.grpc.dao;

import io.clertonraf.grpc.domain.Account;
import io.clertonraf.grpc.domain.Currency;
import io.clertonraf.grpc.domain.Wallet;

import java.util.List;
import java.util.Set;

public interface WalletDAO {

    void save(Wallet wallet);

    void save(Account account);

    Wallet getWalletByIdAndCurrency(int user, Currency currency);

    List<Wallet> getWalletsById(int user);

}
