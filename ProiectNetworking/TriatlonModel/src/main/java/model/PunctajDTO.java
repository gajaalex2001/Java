package model;

import java.io.Serial;
import java.io.Serializable;

public class PunctajDTO implements Serializable {
    private int idproba, idpart, scor, arbitruId;

    public PunctajDTO(int idproba, int idpart, int scor, int arbitruId) {
        this.idproba = idproba;
        this.idpart = idpart;
        this.scor = scor;
        this.arbitruId = arbitruId;
    }

    public int getIdproba() {
        return idproba;
    }

    public void setIdproba(int idproba) {
        this.idproba = idproba;
    }

    public int getIdpart() {
        return idpart;
    }

    public void setIdpart(int idpart) {
        this.idpart = idpart;
    }

    public int getScor() {
        return scor;
    }

    public void setScor(int scor) {
        this.scor = scor;
    }

    public int getArbitruId() {
        return arbitruId;
    }

    public void setArbitruId(int arbitruId) {
        this.arbitruId = arbitruId;
    }
}
