package io.clertonraf.grpc.service.impl;

import io.clertonraf.grpc.dao.WalletDAO;
import io.clertonraf.grpc.dao.impl.WalletDAOImpl;
import io.clertonraf.grpc.domain.Account;
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
    public synchronized String deposit(int user, BigDecimal amount, String currency) {
        WalletDAO dao = WalletDAOImpl.getInstance();
        Account wallet = null; /*= dao.getWalletById(user);*/

        if(wallet == null) {
            wallet = new Account();
            wallet.setUser(user);

           /* switch (Currency.valueOf(currency)) {
                case USD:
                    wallet.setBalanceEUR(BigDecimal.valueOf(0.0));
                    wallet.setBalanceGBR(BigDecimal.valueOf(0.0));
                    wallet.setBalanceUSD(amount);
                    break;
                case GBR:
                    wallet.setBalanceEUR(BigDecimal.valueOf(0.0));
                    wallet.setBalanceGBR(amount);
                    wallet.setBalanceUSD(BigDecimal.valueOf(0.0));
                    break;
                default:
                    wallet.setBalanceEUR(amount);
                    wallet.setBalanceGBR(BigDecimal.valueOf(0.0));
                    wallet.setBalanceUSD(BigDecimal.valueOf(0.0));
            }*/

        } else {
            BigDecimal newBalance;
            /*switch (Currency.valueOf(currency)) {
                case USD:
                    newBalance = wallet.getBalanceUSD().add(amount);
                    wallet.setBalanceUSD(newBalance);
                    break;
                case GBR:
                    newBalance = wallet.getBalanceGBR().add(amount);
                    wallet.setBalanceGBR(newBalance);
                    break;
                default:
                    newBalance = wallet.getBalanceEUR().add(amount);
                    wallet.setBalanceEUR(newBalance);
            }*/

            /*logger.info("My current balance is "+ newBalance.toString());*/
        }

        dao.save(wallet);

        return "ok";
    }


    @Override
    public synchronized String withdraw(int user, BigDecimal amount, String currency) {
        WalletDAO dao = WalletDAOImpl.getInstance();
        Account wallet = null; /*dao.getWalletById(user);*/

        if(wallet == null || amount.subtract(amount).doubleValue() <= 0.0 ) {
            return "insufficient_funds";
        }

        /*BigDecimal newBalance = wallet.getBalance().subtract(amount);
        wallet.setBalance(newBalance);*/

        dao.save(wallet);

        return "ok";
    }

    @Override
    public synchronized BigDecimal getBalance(int user) {
        WalletDAO dao = WalletDAOImpl.getInstance();
        Account wallet = null; /*dao.getWalletById(user);*/

        /*if(wallet == null ) {
            return BigDecimal.valueOf(0.0);
        }

        return wallet.getBalance();*/

        return BigDecimal.valueOf(0.0);
    }
}
