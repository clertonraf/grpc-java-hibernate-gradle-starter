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
        Wallet wallet = dao.getWalletById(user);
        if(wallet == null) {
            wallet = new Wallet();
            wallet.setUser(user);
            wallet.setBalance(amount);
            wallet.setDefaultCurrency("USD");
        }
        BigDecimal newBalance = wallet.getBalance().add(amount);
        wallet.setBalance(newBalance);
        System.out.println("My current balance is "+ newBalance.toString());

        dao.save(wallet);
    }
}
