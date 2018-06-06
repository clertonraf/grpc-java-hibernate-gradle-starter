
import io.clertonraf.grpc.dao.WalletDAO;
import io.clertonraf.grpc.dao.impl.WalletDAOImpl;

import io.clertonraf.grpc.domain.Account;
import io.clertonraf.grpc.domain.Currency;
import io.clertonraf.grpc.domain.Wallet;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


@RunWith(JUnit4.class)
public class WalletDAOTest {

    private WalletDAO walletDao;
    private Wallet walletUSD, walletEUR, walletGBR;
    private Account account;

    @Before
    public void setUp() {
        Set<Wallet> walletSet = new HashSet<>();
        walletDao = WalletDAOImpl.getInstance();
        account = new Account();
        account.setUser(1);

        walletUSD = new Wallet();
        walletUSD.setCurrency(Currency.USD);
        walletUSD.setAccount(account);
        walletUSD.setBalance(BigDecimal.valueOf(0.0));
        walletSet.add(walletUSD);

        walletEUR = new Wallet();
        walletEUR.setCurrency(Currency.EUR);
        walletEUR.setAccount(account);
        walletEUR.setBalance(BigDecimal.valueOf(10.0));
        walletSet.add(walletEUR);

        account.setWallets(walletSet);

        }

    @Test
    public void saveAccount() {
        walletDao.save(account);
        walletDao.save(walletUSD);
        walletDao.save(walletEUR);

        Wallet wallet = walletDao.getWalletByIdAndCurrency(1,Currency.EUR);
        System.out.println(wallet.getBalance());
    }

}
