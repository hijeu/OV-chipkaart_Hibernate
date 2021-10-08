package nl.hu.dp.ovchip.dao;

import nl.hu.dp.ovchip.domein.Adres;
import nl.hu.dp.ovchip.domein.Reiziger;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.List;

public class AdresDAOHibernate implements AdresDAO {
    private Session session;

    public AdresDAOHibernate (Session session) {
        this.session = session;
    }

    @Override
    public boolean save(Adres adres) {
        session.beginTransaction();
        session.save(adres);
        session.getTransaction().commit();

        return session.getTransaction().getStatus() == TransactionStatus.COMMITTED;
    }

    @Override
    public boolean update(Adres adres) {
        return false;
    }

    @Override
    public boolean delete(Adres adres) {
        return false;
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) {
        return null;
    }

    @Override
    public List<Adres> findAll() {
        return null;
    }
}
