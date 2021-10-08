package nl.hu.dp.ovchip.dao;

import nl.hu.dp.ovchip.domein.OVChipkaart;
import nl.hu.dp.ovchip.domein.Product;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOHibernate implements ProductDAO {
    private Session session;

    public ProductDAOHibernate (Session session) {
        this.session = session;
    }

    @Override
    public boolean save(Product product) {
        boolean productSaved = false;

        if (product != null) {
            try {
                if (session.getTransaction().getStatus() != TransactionStatus.ACTIVE) {
                    session.beginTransaction();
                }
                session.save(product);

                session.getTransaction().commit();
                productSaved = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return productSaved;
    }

    @Override
    public boolean update(Product product) {
        boolean productUpdated = false;

        if (product != null) {
            try {
                if (session.getTransaction().getStatus() != TransactionStatus.ACTIVE) {
                    session.beginTransaction();
                }
                session.update(product);
                session.getTransaction().commit();
                productUpdated = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return productUpdated;
    }

    @Override
    public boolean delete(Product product) {
        boolean productDeleted = false;

        if (product != null) {
            try {
                if (session.getTransaction().getStatus() != TransactionStatus.ACTIVE) {
                    session.beginTransaction();
                }
                session.delete(product);
                session.getTransaction().commit();
                productDeleted = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return productDeleted;
    }

    @Override
    public Product findById(int id) {
        return session.get(Product.class, id);
    }

    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) {
        List<Product> producten = new ArrayList<>();

        Query query = session.createQuery("select productnummer from Product, ov_chipkaart where kaartNummer = :kaart_nummer");
        query.setParameter("kaart_nummer", ovChipkaart.getKaartNummer());

        List<Integer> productnummers = query.getResultList();

        for (int productnummer : productnummers) {
            producten.add(findById(productnummer));
        }

        return producten;
    }

    @Override
    public List<Product> findAll() {
        Query query = session.createQuery("from Product");

        return query.getResultList();
    }
}
