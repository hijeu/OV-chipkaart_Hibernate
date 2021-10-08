package nl.hu.dp.ovchip.dao;

import nl.hu.dp.ovchip.domein.OVChipkaart;
import nl.hu.dp.ovchip.domein.Product;
import nl.hu.dp.ovchip.domein.Reiziger;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOHibernate implements OVChipkaartDAO {
    private Session session;
    private ProductDAO pdao;

    public OVChipkaartDAOHibernate (Session session) {
        this.session = session;
    }

    public void setPdao(ProductDAO pdao) {
        this.pdao = pdao;
    }

    @Override
    public boolean save(OVChipkaart ovChipkaart) {
        boolean ovChipkaartSaved = false;

        if (ovChipkaart != null) {
            try {
                if (session.getTransaction().getStatus() != TransactionStatus.ACTIVE) {
                    session.beginTransaction();
                }
                session.save(ovChipkaart);

                List<Product> producten = ovChipkaart.getProducten();
                for (Product product : producten) {
                    pdao.save(product);
                }

                session.getTransaction().commit();
                ovChipkaartSaved = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return ovChipkaartSaved;
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) {
        boolean ovChipkaartSaved = false;

        if (ovChipkaart != null) {
            try {
                if (session.getTransaction().getStatus() != TransactionStatus.ACTIVE) {
                    session.beginTransaction();
                }
                session.update(ovChipkaart);

                List<Product> producten = ovChipkaart.getProducten();
                for (Product product : producten) {
                    pdao.update(product);
                }

                session.getTransaction().commit();
                ovChipkaartSaved = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return ovChipkaartSaved;
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) {
        boolean ovChipkaartDeleted = false;

        if (ovChipkaart != null) {
            try {
                if (session.getTransaction().getStatus() != TransactionStatus.ACTIVE) {
                    session.beginTransaction();
                    session.delete(ovChipkaart);
                    session.getTransaction().commit();
                    ovChipkaartDeleted = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return ovChipkaartDeleted;
    }

    @Override
    public OVChipkaart findById(int id) {
        OVChipkaart ovChipkaart = null;
        Query query = session.createQuery("from ov_chipkaart where kaartNummer = :kaart_nummer");
        query.setParameter("kaart_nummer", id);

        try {
            Object result = query.getSingleResult();

            if (result instanceof OVChipkaart) {
                ovChipkaart = (OVChipkaart) result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ovChipkaart;
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        Query query = session.createQuery("from ov_chipkaart where reiziger = :reiziger");
        query.setParameter("reiziger", reiziger);

        return query.getResultList();
    }

    @Override
    public List<OVChipkaart> findByProduct(Product product) {
        List<OVChipkaart> ovChipkaarten = new ArrayList<>();

        Query query = session.createQuery("select kaartNummer from ov_chipkaart, Product where productnummer = :product_nummer");
        query.setParameter("product_nummer", product.getProductnummer());

        List<Integer> kaartnummers = query.getResultList();

        for (int kaartnummer : kaartnummers) {
            ovChipkaarten.add(findById(kaartnummer));
        }

        return ovChipkaarten;
    }

    @Override
    public List<OVChipkaart> findAll() {
        Query query = session.createQuery("from ov_chipkaart");

        return query.getResultList();
    }
}
