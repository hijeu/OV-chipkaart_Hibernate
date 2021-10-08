package nl.hu.dp.ovchip.dao;

import nl.hu.dp.ovchip.domein.OVChipkaart;
import nl.hu.dp.ovchip.domein.Product;
import org.hibernate.Session;

import java.util.List;

public class ProductDAOHibernate implements ProductDAO {
    private Session session;

    public ProductDAOHibernate (Session session) {
        this.session = session;
    }

    @Override
    public boolean save(Product product) {
        return false;
    }

    @Override
    public boolean update(Product product) {
        return false;
    }

    @Override
    public boolean delete(Product product) {
        return false;
    }

    @Override
    public Product findById(int id) {
        return null;
    }

    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) {
        return null;
    }

    @Override
    public List<Product> findAll() {
        return null;
    }
}
