package nl.hu.dp.ovchip;

import nl.hu.dp.ovchip.dao.*;
import nl.hu.dp.ovchip.domein.Adres;
import nl.hu.dp.ovchip.domein.OVChipkaart;
import nl.hu.dp.ovchip.domein.Product;
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

        rDAOHibernate.setAdao(aDAOHibernate);
        rDAOHibernate.setOvcDao(ovcDAOHibernate);

        aDAOHibernate.setRdao(rDAOHibernate);

        ovcDAOHibernate.setPdao(pDAOHibernate);


        //TEST ReizigerDAOHibernate
        System.out.println("\n---------- Test ReizigerDAOHibernate -------------");
        System.out.println("[Test ReizigerDAOHibernate.findAll() geeft de volgende reizigers:");
        List<Reiziger> reizigers = rDAOHibernate.findAll();
        for (Reiziger reiziger : reizigers) {
            System.out.println(reiziger);
        }

        System.out.println(String.format("\n[Test ReizigerDAOHibernate.findById() geeft de volgende reiziger:\n%s\n",
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
        OVChipkaart ovChipkaart68514 = new OVChipkaart(68514, geldigTotDatum, 1, 2.50, hLubben);
        System.out.println(rDAOHibernate.findByOVChipkaart(ovChipkaart68514));

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
        rDAOHibernate.update(hieu);
        System.out.println("Na ReizigerDAOHibernate.update() zijn de gegevens:\n" + rDAOHibernate.findById(6));

        System.out.println("\n[Test ReizigerDAOHibernate.delete()]");
        System.out.print(String.format("Aantal reizigers: %d, ", rDAOHibernate.findAll().size()));
        rDAOHibernate.delete(rDAOHibernate.findById(6));
        System.out.println("na ReizigerDAOHibernate.delete(): " + rDAOHibernate.findAll().size());


        //TEST AdresDAOHibernate
        System.out.println("\n---------- Test AdresDAOHibernate -------------");
        System.out.println("[Test AdresDAOHibernate.findAll() geeft de volgende adressen:");
        List<Adres> adressen = aDAOHibernate.findAll();
        for (Adres adres : adressen) {
            System.out.println(adres);
        }

        System.out.println("\n[Test AdresDAOHibernate.findByReiziger() geeft de volgende adres:");
        System.out.println(aDAOHibernate.findByReiziger(rDAOHibernate.findById(1)));

        System.out.println("\n[Test AdresDAOHibernate.save()]");
        System.out.print(String.format("Aantal adressen: %d, ", aDAOHibernate.findAll().size()));
        rDAOHibernate.save(hieu);
        Adres adres = new Adres(6, "3607BL", "556", "Duivenkamp", "Maarssen", hieu);
        aDAOHibernate.save(adres);
        System.out.println("na AdresDAOHibernate.save(): " + aDAOHibernate.findAll().size());

        System.out.println("\n[Test ReizigerDAOHibernate.update()]");
        System.out.println("Gegevens van adres van reiziger " + rDAOHibernate.findById(6).getVoorletters() + ":\n"
                + aDAOHibernate.findByReiziger(rDAOHibernate.findById(6)));
        adres.setHuisnummer("551");
        aDAOHibernate.update(adres);
        System.out.println("Na AdresDAOHibernate.update() zijn de gegevens:\n" + aDAOHibernate.findByReiziger(rDAOHibernate.findById(6)));

        System.out.println("\n[Test AdresDAOHibernate.delete()]");
        System.out.print(String.format("Aantal adressen: %d, ", aDAOHibernate.findAll().size()));
        aDAOHibernate.delete(aDAOHibernate.findByReiziger(rDAOHibernate.findById(6)));
        System.out.println("na AdresDAOHibernate.delete(): " + aDAOHibernate.findAll().size());
        rDAOHibernate.delete(rDAOHibernate.findById(6));


        //TEST OVChipkaartDAOHibernate
        System.out.println("\n---------- Test OVChipkaartDAOHibernate -------------");
        System.out.println("[Test OVChipkaartDAOHibernate.findAll() geeft de volgende ovchipkaarten:");
        List<OVChipkaart> ovChipkaarten = ovcDAOHibernate.findAll();
        for (OVChipkaart ovChipkaart : ovChipkaarten) {
            System.out.println(ovChipkaart);
        }

        System.out.println(String.format("\n[Test OVCDAOHibernate.findById() geeft de volgende ovchipkaart:\n%s",
                ovcDAOHibernate.findById(35283)));

        geboortedatum = java.sql.Date.valueOf("2002-09-17");
        Reiziger bVanRijn = new Reiziger(2, "B", "van", "Rijn", geboortedatum);
        System.out.println(String.format("\n[Test OVCDAOHibernate.findByReiziger() geeft de volgende ovchipkaarten:"));
        ovChipkaarten = ovcDAOHibernate.findByReiziger(bVanRijn);
        for (OVChipkaart ovChipkaart : ovChipkaarten) {
            System.out.println(ovChipkaart);
        }

        Product product3 = new Product(3, "Dagkaart 2e klas", "Een hele dag onbeperkt reizen met de trein.", 50.60
);
        System.out.println(String.format("\n[Test OVCDAOHibernate.findByProduct() geeft de volgende ovchipkaarten:"));
        ovChipkaarten = ovcDAOHibernate.findByProduct(product3);
        for (OVChipkaart ovChipkaart : ovChipkaarten) {
            System.out.println(ovChipkaart);
        }


//        System.out.println("\n[Test ReizigerDAOHibernate.save()]");
//        System.out.print(String.format("Aantal reizigers: %d, ", rDAOHibernate.findAll().size()));
//        geboortedatum = java.sql.Date.valueOf("1999-03-13");
//        Reiziger hieu = new Reiziger(6, "CHM", null, "Bui", geboortedatum);
//        rDAOHibernate.save(hieu);
//        System.out.println("na ReizigerDAOHibernate.save(): " + rDAOHibernate.findAll().size());
//
//        System.out.println("\n[Test ReizigerDAOHibernate.update()]");
//        System.out.println("Gegevens van reiziger met reiziger_id #6:\n" + rDAOHibernate.findById(6));
//        hieu.setTussenvoegsel("van de");
//        hieu.setAchternaam("Buurt");
//        rDAOHibernate.update(hieu);
//        System.out.println("Na ReizigerDAOHibernate.update() zijn de gegevens:\n" + rDAOHibernate.findById(6));
//
//        System.out.println("\n[Test ReizigerDAOHibernate.delete()]");
//        System.out.print(String.format("Aantal reizigers: %d, ", rDAOHibernate.findAll().size()));
//        rDAOHibernate.delete(rDAOHibernate.findById(6));
//        System.out.println("na ReizigerDAOHibernate.delete(): " + rDAOHibernate.findAll().size());


        //TEST ProductDAOHibernate
        System.out.println("\n---------- Test ProductDAOHibernate -------------");


        session.close();
    }
}