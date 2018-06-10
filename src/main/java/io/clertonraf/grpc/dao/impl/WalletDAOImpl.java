package io.clertonraf.grpc.dao.impl;

import io.clertonraf.grpc.WalletClient;
import io.clertonraf.grpc.dao.WalletDAO;
import io.clertonraf.grpc.domain.Account;
import io.clertonraf.grpc.domain.Currency;
import io.clertonraf.grpc.domain.Wallet;
import io.clertonraf.grpc.domain.WalletPK;
import io.clertonraf.grpc.util.HibernateUtil;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
import org.hibernate.mapping.PrimaryKey;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WalletDAOImpl implements WalletDAO {

    private static final Logger logger = Logger.getLogger(WalletDAOImpl.class.getName());

    private WalletDAOImpl(){}

    private static class WalletDAOImplHelper {
        private static final WalletDAOImpl INSTANCE = new WalletDAOImpl();
    }

    public static WalletDAOImpl getInstance() {
        return WalletDAOImplHelper.INSTANCE;
    }

    private Session getSession() {
        return HibernateUtil.getSessionFactory().openSession();
    }

    @Override
    public void save(Wallet wallet) {
        Session session = getSession();
        session.beginTransaction();
        try {
            session.saveOrUpdate(wallet);
            session.getTransaction().commit();
        } catch (StaleObjectStateException sose) {
            logger.log(Level.SEVERE,"Lock",sose);
            session.buildLockRequest(new LockOptions(LockMode.OPTIMISTIC_FORCE_INCREMENT));
        }

        session.close();
    }

    @Override
    public void save(Account account) {
        Session session = getSession();
        session.beginTransaction();
        session.saveOrUpdate(account);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public Wallet getWalletByIdAndCurrency(int user, Currency currency) {
        Session session = this.getSession();
        session.beginTransaction();

        Wallet wallet = (Wallet) session.createCriteria(Wallet.class)
                .setFetchMode("walletPK.account", FetchMode.JOIN)
                .add(
                        Restrictions.and(
                                Restrictions.eq("walletPK.currency",currency),
                                Restrictions.eq("walletPK.account.user",user)
                        )
                )
                .uniqueResult();
        session.close();
        return wallet;

    }

    @Override
    public List<Wallet> getWalletsById(int user) {
        Session session = this.getSession();
        session.beginTransaction();

        List<Wallet> walletSet = (List<Wallet>) session.createCriteria(Wallet.class)
                .setFetchMode("walletPK.account", FetchMode.JOIN)
                .add(
                        Restrictions.eq("walletPK.account.user",user)
                )
                .list();
        session.close();
        return walletSet;

    }


}
