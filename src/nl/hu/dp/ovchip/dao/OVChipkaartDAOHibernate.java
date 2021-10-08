package nl.hu.dp.ovchip.dao;

import nl.hu.dp.ovchip.domein.OVChipkaart;
import nl.hu.dp.ovchip.domein.Product;
import nl.hu.dp.ovchip.domein.Reiziger;
import org.hibernate.Session;

import java.util.List;
import java.util.Set;

public class OVChipkaartDAOHibernate implements OVChipkaartDAO {
    private Session session;

    public OVChipkaartDAOHibernate (Session session) {
        this.session = session;
    }

    @Override
    public boolean save(OVChipkaart ovChipkaart) {
        return false;
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) {
        return false;
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) {
        return false;
    }

    @Override
    public OVChipkaart findById(int id) {
        return null;
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        return null;
    }

    @Override
    public List<OVChipkaart> findByProduct(Product product) {
        return null;
    }

    @Override
    public List<OVChipkaart> findAll() {
        return null;
    }
}
