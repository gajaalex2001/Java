package model;

import java.io.Serializable;

public class Arbitru implements Identifiable<Integer>, Serializable {
    private int ID;
    private String nume;
    private String prenume;
    private Proba proba;

    public Arbitru(int ID, String nume, String prenume, Proba proba) {
        this.ID = ID;
        this.nume = nume;
        this.prenume = prenume;
        this.proba = proba;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public Proba getProba() {
        return proba;
    }

    public void setProba(Proba proba) {
        this.proba = proba;
    }

    @Override
    public String toString() {
        return '{' + String.valueOf(ID) + " " + nume + " " + prenume + " " + proba.getNume() + '}';
    }
}
