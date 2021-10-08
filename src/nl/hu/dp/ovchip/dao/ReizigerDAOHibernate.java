package nl.hu.dp.ovchip.dao;

import nl.hu.dp.ovchip.domein.OVChipkaart;
import nl.hu.dp.ovchip.domein.Reiziger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOHibernate implements ReizigerDAO {
    private Session session;

    public ReizigerDAOHibernate (Session session) {
        this.session = session;
    }

    @Override
    public boolean save(Reiziger reiziger) {
        boolean reizigerSaved = false;

        try {
            session.beginTransaction();
            session.save(reiziger);
            session.getTransaction().commit();
            reizigerSaved = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reizigerSaved;
    }

    @Override
    public boolean update(Reiziger reiziger) {
        session.beginTransaction();
        session.update(reiziger);
        session.getTransaction().commit();

        return session.getTransaction().getStatus() == TransactionStatus.COMMITTED;
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        session.beginTransaction();
        session.delete(reiziger);
        session.getTransaction().commit();

        return session.getTransaction().getStatus() == TransactionStatus.COMMITTED;
    }

    @Override
    public Reiziger findById(int id) {
        Reiziger reiziger;

        reiziger = session.get(Reiziger.class,
                id);
        Hibernate.initialize(reiziger);

        return reiziger;
    }

    @Override
    public List<Reiziger> findByGbdatum(String datum) {
        Query query = session.createQuery("from Reiziger where geboortedatum = :geboortedatum");
        query.setParameter("geboortedatum", java.sql.Date.valueOf(datum));

        return new ArrayList<Reiziger>(query.getResultList());
    }

    @Override
    public Reiziger findByOVChipkaart(OVChipkaart ovChipkaart) {
        int reiziger_id = ovChipkaart.getReiziger().getReizigernummer();

        return findById(reiziger_id);
    }

    @Override
    public List<Reiziger> findAll() {
        Query query = session.createQuery("from Reiziger");

        return new ArrayList<Reiziger>(query.getResultList());
    }
}
