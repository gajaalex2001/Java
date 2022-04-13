package model;

import java.io.Serializable;

public class RaportDTO implements Serializable, Comparable<RaportDTO> {
    private int ID;
    private String nume;
    private String prenume;
    private int punctaj;

    public RaportDTO(int ID, String nume, String prenume, int punctaj) {
        this.ID = ID;
        this.nume = nume;
        this.prenume = prenume;
        this.punctaj = punctaj;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
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

    public int getPunctaj() {
        return punctaj;
    }

    public void setPunctaj(int punctaj) {
        this.punctaj = punctaj;
    }

    @Override
    public int compareTo(RaportDTO other){
        if(punctaj > other.punctaj) return 1;
        return -1;
    }
}
