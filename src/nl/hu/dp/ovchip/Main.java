package nl.hu.dp.ovchip;

import nl.hu.dp.ovchip.dao.*;
import nl.hu.dp.ovchip.domein.OVChipkaart;
import nl.hu.dp.ovchip.domein.Reiziger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * Testklasse - deze klasse test alle andere klassen in deze package.
 *
 * System.out.println() is alleen in deze klasse toegestaan (behalve voor exceptions).
 *
 * @author tijmen.muller@hu.nl
 */
public class Main {
    // Creëer een factory voor Hibernate sessions.
    private static final SessionFactory factory;

    static {
        try {
            // Create a Hibernate session factory
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Retouneer een Hibernate session.
     *
     * @return Hibernate session
     * @throws HibernateException
     */
    private static Session getSession() throws HibernateException {
        return factory.openSession();
    }

    public static void main(String[] args) throws SQLException {
        //testFetchAll();
        testDAOHibernate();
    }

    /**
     * P6. Haal alle (geannoteerde) entiteiten uit de database.
     */
    private static void testFetchAll() {
        Session session = getSession();
        try {
            Metamodel metamodel = session.getSessionFactory().getMetamodel();
            for (EntityType<?> entityType : metamodel.getEntities()) {
                Query query = session.createQuery("from " + entityType.getName());

                System.out.println("[Test] Alle objecten van type " + entityType.getName() + " uit database:");
                for (Object o : query.list()) {
                    System.out.println("  " + o);
                }
                System.out.println();
            }
        } finally {
            session.close();
        }
    }

    private static void testDAOHibernate() {
        Session session = getSession();

        ReizigerDAOHibernate rDAOHibernate = new ReizigerDAOHibernate(session);
        AdresDAOHibernate aDAOHibernate = new AdresDAOHibernate(session);
        OVChipkaartDAOHibernate ovcDAOHibernate = new OVChipkaartDAOHibernate(session);
        ProductDAOHibernate pDAOHibernate = new ProductDAOHibernate(session);

        System.out.println("\n---------- Test ReizigerDAOHibernate -------------");
        System.out.println("[Test ReizigerDAOHibernate.findAll() geeft de volgende reizigers:");
        List<Reiziger> reizigers = rDAOHibernate.findAll();
        for (Reiziger reiziger : reizigers) {
            System.out.println(reiziger);
        }

        System.out.println(String.format("\n[Test ReizigerDAOHibernate.findById(5) geeft de volgende reiziger:\n%s\n",
                rDAOHibernate.findById(5)));

        System.out.println("[Test ReizigerDAOHibernate.findByGbdatum(\"2002-10-22\") geeft de volgende reizigers:");
        List<Reiziger> reizigersGevondenOpGb = rDAOHibernate.findByGbdatum("2002-10-22");
        for (Reiziger reiziger : reizigersGevondenOpGb) {
            System.out.println(reiziger);
        }

        System.out.println("\n[Test ReizigerDAOHibernate.findByOVChipkaart() geeft de volgende reiziger:");
        Date geldigTotDatum = java.sql.Date.valueOf("2020-03-31");
        Date geboortedatum = java.sql.Date.valueOf("1998-08-11");
        Reiziger hLubben = new Reiziger(3, "H", null, "Lubben",geboortedatum);
        OVChipkaart ovChipkaart = new OVChipkaart(68514, geldigTotDatum, 1, 2.50, hLubben);
        System.out.println(rDAOHibernate.findByOVChipkaart(ovChipkaart));

        System.out.println("\n[Test ReizigerDAOHibernate.save()]");
        System.out.print(String.format("Aantal reizigers: %d, ", rDAOHibernate.findAll().size()));
        geboortedatum = java.sql.Date.valueOf("1999-03-13");
        Reiziger hieu = new Reiziger(6, "CHM", null, "Bui", geboortedatum);
        rDAOHibernate.save(hieu);
        System.out.println("na ReizigerDAOHibernate.save(): " + rDAOHibernate.findAll().size());

        System.out.println("\n[Test ReizigerDAOHibernate.update()]");
        System.out.println("Gegevens van reiziger met reiziger_id #6:\n" + rDAOHibernate.findById(6));
        hieu.setTussenvoegsel("van de");
        hieu.setAchternaam("Buurt");
        System.out.println("Na ReizigerDAOHibernate.update() zijn de gegevens:\n" + rDAOHibernate.findById(6));

        System.out.println("\n[Test ReizigerDAOHibernate.delete()]");
        System.out.print(String.format("Aantal reizigers: %d, ", rDAOHibernate.findAll().size()));
        rDAOHibernate.delete(rDAOHibernate.findById(6));
        System.out.println("na ReizigerDAOHibernate.delete(): " + rDAOHibernate.findAll().size());

        session.close();
    }
}