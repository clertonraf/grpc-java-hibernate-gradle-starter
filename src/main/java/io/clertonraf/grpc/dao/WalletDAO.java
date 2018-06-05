package io.clertonraf.grpc.dao;

import io.clertonraf.grpc.domain.Wallet;

public interface WalletDAO {

    void save(Wallet wallet);

    Wallet getWalletById(String user);

}
