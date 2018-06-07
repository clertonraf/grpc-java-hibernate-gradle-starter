package io.clertonraf.grpc.service;

import java.math.BigDecimal;
import java.util.Map;

public interface WalletService {

    String deposit(int user, BigDecimal amount, String currency);

    String withdraw(int user, BigDecimal amount, String currency);

    Map<String, BigDecimal> getBalance(int user);
}
