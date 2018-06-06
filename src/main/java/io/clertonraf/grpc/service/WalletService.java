package io.clertonraf.grpc.service;

import java.math.BigDecimal;

public interface WalletService {

    String deposit(int user, BigDecimal amount, String currency);

    String withdraw(int user, BigDecimal amount, String currency);

    BigDecimal getBalance(int user);
}
