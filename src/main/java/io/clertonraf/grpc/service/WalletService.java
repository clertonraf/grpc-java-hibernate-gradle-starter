package io.clertonraf.grpc.service;

import java.math.BigDecimal;

public interface WalletService {

    String deposit(String user, BigDecimal amount, String currency);

    String withdraw(String user, BigDecimal amount, String currency);

    String getBalance(String user);
}
