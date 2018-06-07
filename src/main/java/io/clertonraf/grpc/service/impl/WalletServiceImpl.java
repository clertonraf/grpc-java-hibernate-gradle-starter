package io.clertonraf.grpc.service.impl;

import io.clertonraf.grpc.dao.WalletDAO;
import io.clertonraf.grpc.dao.impl.WalletDAOImpl;
import io.clertonraf.grpc.domain.Account;
import io.clertonraf.grpc.domain.Currency;
import io.clertonraf.grpc.domain.Wallet;
import io.clertonraf.grpc.domain.WalletPK;
import io.clertonraf.grpc.service.WalletService;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class WalletServiceImpl implements WalletService {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(WalletServiceImpl.class.getName());

    public WalletServiceImpl() {}

    private static class WalletServiceImplHelper {
        private static final WalletServiceImpl INSTANCE = new WalletServiceImpl();
    }

    public static WalletServiceImpl getInstance() {
        return WalletServiceImplHelper.INSTANCE;
    }

    private void createAccount(int user, BigDecimal amount, String currency) {
        Account account = new Account();
        account.setUser(user);

        Set<Wallet> walletSet = new HashSet<>();

        for (Currency c : Currency.values()) {
            Wallet wallet = new Wallet();

            WalletPK walletId = new WalletPK();
            walletId.setAccount(account);
            walletId.setCurrency(c);
            wallet.setWalletPK(walletId);

            wallet.setBalance(c.name().equalsIgnoreCase(currency) ? amount : BigDecimal.valueOf(0.0));

            walletSet.add(wallet);
        }

        account.setWallets(walletSet);

        WalletDAO dao = WalletDAOImpl.getInstance();
        dao.save(account);

        walletSet.stream().forEach(w -> WalletDAOImpl.getInstance().save(w));
    }

    @Override
    public synchronized String deposit(int user, BigDecimal amount, String currency) {
        WalletDAO dao = WalletDAOImpl.getInstance();
        Wallet wallet = dao.getWalletByIdAndCurrency(user, Currency.valueOf(currency));

        if(wallet == null) {
            this.createAccount(user,amount,currency);
        } else {
            BigDecimal newBalance = wallet.getBalance().add(amount);
            wallet.setBalance(newBalance);
            dao.save(wallet);
        }

        return "ok";
    }


    @Override
    public synchronized String withdraw(int user, BigDecimal amount, String currency) {
        WalletDAO dao = WalletDAOImpl.getInstance();
        Wallet wallet = dao.getWalletByIdAndCurrency(user,Currency.valueOf(currency));

        if(wallet == null || wallet.getBalance().subtract(amount).doubleValue() < 0.0 ) {
            return "insufficient_funds";
        }

        BigDecimal newBalance = wallet.getBalance().subtract(amount);
        wallet.setBalance(newBalance);

        dao.save(wallet);

        return "ok";
    }

    @Override
    public synchronized Map<String, BigDecimal> getBalance(int user) {
        WalletDAO dao = WalletDAOImpl.getInstance();
        List<Wallet> wallets = dao.getWalletsById(user);

        return wallets.stream().collect(Collectors.toMap(w -> w.getWalletPK().getCurrency().name(), w -> w.getBalance()));
    }
}
