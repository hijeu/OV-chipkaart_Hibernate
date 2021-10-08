package nl.hu.dp.ovchip.dao;

import nl.hu.dp.ovchip.domein.Adres;
import nl.hu.dp.ovchip.domein.Reiziger;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import javax.persistence.Query;
import java.util.List;

public class AdresDAOHibernate implements AdresDAO {
    private Session session;
    private ReizigerDAO rdao;

    public AdresDAOHibernate (Session session) {
        this.session = session;
    }

    public void setRdao (ReizigerDAO rdao) {
        this.rdao = rdao;
    }

    @Override
    public boolean save(Adres adres) {
        boolean adresSaved = false;

        if (adres != null) {
            try {
                if (session.getTransaction().getStatus() != TransactionStatus.ACTIVE) {
                    session.beginTransaction();
                }
                session.save(adres);
                session.getTransaction().commit();
                adresSaved = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return adresSaved;
    }

    @Override
    public boolean update(Adres adres) {
        boolean adresUpdated = false;

        if (adres != null) {
            try {
                if (session.getTransaction().getStatus() != TransactionStatus.ACTIVE) {
                    session.beginTransaction();
                }
                session.update(adres);
                session.getTransaction().commit();
                adresUpdated = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return adresUpdated;
    }

    @Override
    public boolean delete(Adres adres) {
        boolean adresDeleted = false;

        if (adres != null) {
            try {
                if (session.getTransaction().getStatus() != TransactionStatus.ACTIVE) {
                    session.beginTransaction();
                }
                session.delete(adres);
                session.getTransaction().commit();
                adresDeleted = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return adresDeleted;
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) {
        Adres adres = null;
        Query query = session.createQuery("from Adres where reiziger = :reiziger");
        query.setParameter("reiziger", reiziger);

        try {
            Object result = query.getSingleResult();

            if (result instanceof Adres) {
                adres = (Adres) result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return adres;
    }

    @Override
    public List<Adres> findAll() {
        Query query = session.createQuery("from Adres");

        return query.getResultList();
    }
}
