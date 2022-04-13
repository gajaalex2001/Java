package model;

import java.io.Serializable;

public class Proba implements Identifiable<Integer>, Serializable {
    private int ID;
    private String nume;

    public Proba(int ID, String nume) {
        this.ID = ID;
        this.nume = nume;
    }

    @Override
    public Integer getID() {
        return ID;
    }

    @Override
    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    @Override
    public String toString() {
        return "{" + String.valueOf(ID) + " " + nume + '}';
    }
}
