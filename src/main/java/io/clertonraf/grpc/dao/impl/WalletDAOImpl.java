package io.clertonraf.grpc.dao.impl;

import io.clertonraf.grpc.dao.WalletDAO;
import io.clertonraf.grpc.domain.Wallet;
import io.clertonraf.grpc.util.HibernateUtil;
import org.hibernate.Session;

public class WalletDAOImpl implements WalletDAO {

    private Session getSession() {
        return HibernateUtil.getSessionFactory().openSession();
    }

    @Override
    public void save(Wallet wallet) {
        Session session = getSession();
        session.beginTransaction();
        session.save(wallet);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public Wallet getWalletById(String user) {
        Session session = getSession();
        session.beginTransaction();
        Wallet wallet = (Wallet)this.getSession().get(Wallet.class, user);
        return wallet;
    }


}
