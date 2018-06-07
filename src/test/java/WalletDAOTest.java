
import io.clertonraf.grpc.dao.WalletDAO;
import io.clertonraf.grpc.dao.impl.WalletDAOImpl;

import io.clertonraf.grpc.domain.Account;
import io.clertonraf.grpc.domain.Currency;
import io.clertonraf.grpc.domain.Wallet;
import io.clertonraf.grpc.domain.WalletPK;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RunWith(JUnit4.class)
public class WalletDAOTest {

    private WalletDAO walletDao;
    private Wallet walletUSD, walletEUR, walletGBR;
    private Account account1;

    @Before
    public void setUp() {
        Set<Wallet> walletSet = new HashSet<>();
        walletDao = WalletDAOImpl.getInstance();
        account1 = new Account();
        account1.setUser(1);

        walletUSD = new Wallet();
        WalletPK walletUSDId = new WalletPK();
        walletUSDId.setCurrency(Currency.USD);
        walletUSDId.setAccount(account1);
        walletUSD.setWalletPK(walletUSDId);
        walletUSD.setBalance(BigDecimal.valueOf(0.0));
        walletSet.add(walletUSD);

        walletEUR = new Wallet();
        WalletPK walletEURId = new WalletPK();
        walletEURId.setCurrency(Currency.EUR);
        walletEURId.setAccount(account1);
        walletEUR.setWalletPK(walletEURId);
        walletEUR.setBalance(BigDecimal.valueOf(10.0));
        walletSet.add(walletEUR);

        walletGBR = new Wallet();
        WalletPK walletGBRId = new WalletPK();
        walletGBRId.setCurrency(Currency.GBR);
        walletGBRId.setAccount(account1);
        walletGBR.setWalletPK(walletGBRId);
        walletGBR.setBalance(BigDecimal.valueOf(1.0));
        walletSet.add(walletGBR);

        account1.setWallets(walletSet);

        }

    @Test
    public void saveAccount_and_retrieve_wallets() {
        walletDao.save(account1);
        walletDao.save(walletUSD);
        walletDao.save(walletEUR);
        walletDao.save(walletGBR);

        Wallet walletEUR = walletDao.getWalletByIdAndCurrency(1,Currency.EUR);
        Assert.assertEquals(10.00, walletEUR.getBalance().doubleValue(), 0.0000001);

        Wallet walletGBR = walletDao.getWalletByIdAndCurrency(1,Currency.GBR);
        Assert.assertEquals(1.00, walletGBR.getBalance().doubleValue(), 0.0000001);

        Wallet walletUSD = walletDao.getWalletByIdAndCurrency(1,Currency.USD);
        Assert.assertEquals(0.00, walletUSD.getBalance().doubleValue(), 0.0000001);

        List<Wallet> walletSet = walletDao.getWalletsById(1);
        Assert.assertNotNull(walletSet);

        Assert.assertEquals(3,walletSet.size());
    }

}
