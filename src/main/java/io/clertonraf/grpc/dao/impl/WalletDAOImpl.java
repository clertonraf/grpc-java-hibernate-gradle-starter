package io.clertonraf.grpc.dao.impl;

import io.clertonraf.grpc.dao.WalletDAO;
import io.clertonraf.grpc.domain.Account;
import io.clertonraf.grpc.domain.Currency;
import io.clertonraf.grpc.domain.Wallet;
import io.clertonraf.grpc.domain.WalletPK;
import io.clertonraf.grpc.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.mapping.PrimaryKey;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public class WalletDAOImpl implements WalletDAO {

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
        session.saveOrUpdate(wallet);
        session.getTransaction().commit();
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
        Session session = getSession();
        session.beginTransaction();

        /*Account account = (Account) this.getSession().get(Account.class,user);
        WalletPK walletPK = new WalletPK();
        walletPK.setCurrency(currency);
        walletPK.setAccount(account);
        return (Wallet)this.getSession().get(Wallet.class, walletPK);*/
        /*return (Wallet) this.getSession()
                .createCriteria(Wallet.class)
                .add(Restrictions.eq("walletPK.user",user))
                .uniqueResult();*/
    }


}
