package model;

import java.io.Serializable;
import java.util.List;

public class Participant implements Identifiable<Integer>, Serializable {
    private int ID;
    private String nume;
    private String prenume;
    private List<Proba> probe;
    private int punctaj;

    public Participant(int ID, String nume, String prenume, List<Proba> probe, int punctaj) {
        this.ID = ID;
        this.nume = nume;
        this.prenume = prenume;
        this.probe = probe;
        this.punctaj = punctaj;
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

    public List<Proba> getProbe() {
        return probe;
    }

    public void setProbe(List<Proba> probe) {
        this.probe = probe;
    }

    public int getPunctaj() {
        return punctaj;
    }

    public void setPunctaj(int punctaj) {
        this.punctaj = punctaj;
    }

    @Override
    public String toString() {
        return "{" + String.valueOf(ID) + " " + nume + " " + prenume + " " + probe + " " + punctaj + '}';
    }
}

