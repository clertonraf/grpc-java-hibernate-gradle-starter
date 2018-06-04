package io.clertonraf.grpc.service;

import java.math.BigDecimal;

public interface WalletService {

    void deposit(String user, BigDecimal amount, String currency);
}
