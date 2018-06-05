package io.clertonraf.grpc.service.impl;

import io.clertonraf.grpc.dao.WalletDAO;
import io.clertonraf.grpc.dao.impl.WalletDAOImpl;
import io.clertonraf.grpc.domain.Currency;
import io.clertonraf.grpc.domain.Wallet;
import io.clertonraf.grpc.service.WalletService;

import java.math.BigDecimal;

public class WalletServiceImpl implements WalletService {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(WalletServiceImpl.class.getName());

    public WalletServiceImpl() {}

    private static class WalletServiceImplHelper {
        private static final WalletServiceImpl INSTANCE = new WalletServiceImpl();
    }

    public static WalletServiceImpl getInstance() {
        return WalletServiceImplHelper.INSTANCE;
    }

    @Override
    public synchronized String deposit(String user, BigDecimal amount, String currency) {
        WalletDAO dao = WalletDAOImpl.getInstance();
        Wallet wallet = dao.getWalletById(user);

        if(wallet == null) {
            wallet = new Wallet();
            wallet.setUser(user);
            wallet.setBalance(amount);
            wallet.setDefaultCurrency(Currency.EUR.name());
        }

        BigDecimal newBalance = wallet.getBalance().add(amount);
        wallet.setBalance(newBalance);

        logger.info("My current balance is "+ newBalance.toString());

        dao.save(wallet);

        return "ok";
    }

    @Override
    public synchronized String withdraw(String user, BigDecimal amount, String currency) {
        WalletDAO dao = WalletDAOImpl.getInstance();
        Wallet wallet = dao.getWalletById(user);

        if(wallet == null || amount.compareTo(wallet.getBalance()) <= 0 ) {
            return "insufficient_funds";
        }

        BigDecimal newBalance = wallet.getBalance().add(amount);
        wallet.setBalance(newBalance);

        dao.save(wallet);

        return "ok";
    }

    @Override
    public synchronized String getBalance(String user) {
        WalletDAO dao = WalletDAOImpl.getInstance();
        Wallet wallet = dao.getWalletById(user);

        if(wallet == null ) {
            return "0.0";
        }

        return wallet.getBalance().toString();
    }
}
