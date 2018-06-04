package io.clertonraf.grpc.service.impl;

import io.clertonraf.grpc.dao.WalletDAO;
import io.clertonraf.grpc.dao.impl.WalletDAOImpl;
import io.clertonraf.grpc.domain.Wallet;
import io.clertonraf.grpc.service.WalletService;

import java.math.BigDecimal;

public class WalletServiceImpl implements WalletService {

    @Override
    public void deposit(String user, BigDecimal amount, String currency) {
        WalletDAO dao = new WalletDAOImpl();
        Wallet wallet = new Wallet();
        BigDecimal balance = dao.getWalletById(user).getBalance();
        System.out.println("My current balance is "+balance);
        wallet.setUser(user);
        wallet.setBalance(amount);
        wallet.setDefaultCurrency("USD");
        dao.save(wallet);
    }
}
