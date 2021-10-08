package nl.hu.dp.ovchip.domein;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class Product {
    @Id
    @Column(name = "product_nummer")
    private int productnummer;
    private String naam;
    private String beschrijving;
    private double prijs;

    @ManyToMany(mappedBy = "producten")
    private List<OVChipkaart> ovChipkaarten = new ArrayList<OVChipkaart>() {
    };

    public Product(int productNummer, String naam, String beschrijving, double prijs) {
        this.productnummer = productNummer;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
    }

    public Product() {
    }

    public int getProductnummer() {
        return productnummer;
    }

    public void setProductnummer(int productNummer) {
        this.productnummer = productNummer;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public double getPrijs() {
        return prijs;
    }

    public void setPrijs(double prijs) {
        this.prijs = prijs;
    }

    public List<OVChipkaart> getOvChipkaarten() {
        return ovChipkaarten;
    }

    public void setOvChipkaarten(List<OVChipkaart> ovChipkaarten) {
        this.ovChipkaarten = ovChipkaarten;
    }

    public void addOVChipkaart(OVChipkaart ovChipkaart) {
        ovChipkaarten.add(ovChipkaart);
    }

    public void removeOVChipkaart(OVChipkaart ovChipkaart) {
            ovChipkaarten.remove(ovChipkaart);
    }

    public boolean equals(Object andereObject) {
        boolean gelijkeObjecten = false;

        if (andereObject instanceof Product) {
             Product andereProduct = (Product) andereObject;

            if (this.productnummer == andereProduct.getProductnummer() &&
                this.naam.equals(andereProduct.getNaam()) &&
                this.beschrijving.equals(andereProduct.getBeschrijving()) &&
                this.prijs == andereProduct.getPrijs() &&
                this.ovChipkaarten.equals(andereProduct.getOvChipkaarten())) {
                gelijkeObjecten = true;
            }
        }

        return gelijkeObjecten;
    }

    public String toString() {
        return String.format("Product {#%d, Naam: %s, Beschrijving: \"%s\", prijs: €%.2f}",
                getProductnummer(), getNaam(), getBeschrijving(), getPrijs());
    }
}
