package services;

import model.*;

import java.util.List;

public interface ITriatlonServices {
    Arbitru login(ArbitruDTO dto, ITriatlonObserver client) throws TriatlonException;
    void logout(Arbitru dto, ITriatlonObserver client) throws TriatlonException;
    void addPunctaj(PunctajDTO dto, ITriatlonObserver client) throws TriatlonException;
    List<Participant> getParticipantiProba(int pid, ITriatlonObserver client) throws TriatlonException;
    List<RaportDTO> getRaportParticipanti(int pid, ITriatlonObserver client) throws TriatlonException;
}
