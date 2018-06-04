package io.clertonraf.grpc.dao.impl;

import io.clertonraf.grpc.dao.WalletDAO;
import io.clertonraf.grpc.domain.Wallet;
import io.clertonraf.grpc.util.HibernateUtil;
import org.hibernate.Session;

public class WalletDAOImpl implements WalletDAO {
    @Override
    public void save(Wallet wallet) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.saveOrUpdate(wallet);
        session.getTransaction().commit();
        session.close();
    }
}
