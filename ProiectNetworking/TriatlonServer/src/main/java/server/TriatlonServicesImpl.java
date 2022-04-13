package server;

import model.*;
import repository.*;
import services.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TriatlonServicesImpl implements ITriatlonServices{
    private ArbitruRepository repoArbitru;
    private ProbaRepository repoProba;
    private ParticipantRepository repoPart;
    private Map<Integer, ITriatlonObserver> loggedClients;

    public TriatlonServicesImpl(ArbitruRepository repoArbitru, ProbaRepository repoProba, ParticipantRepository repoPart) {
        this.repoArbitru = repoArbitru;
        this.repoProba = repoProba;
        this.repoPart = repoPart;
        loggedClients = new ConcurrentHashMap<>();
    }

    public synchronized Arbitru login(ArbitruDTO dto, ITriatlonObserver client) throws TriatlonException {
        Arbitru arbitru=repoArbitru.getAccount(dto.getUser(), dto.getPass());
        if (arbitru!=null){
            if(loggedClients.get(arbitru.getID())!=null)
                throw new TriatlonException("User already logged in.");
            loggedClients.put(arbitru.getID(), client);
            return arbitru;
        }else
            throw new TriatlonException("Authentication failed.");
    }


    public synchronized void logout(Arbitru arb, ITriatlonObserver client) throws TriatlonException {
        ITriatlonObserver localClient=loggedClients.remove(arb.getID());
        if (localClient==null)
            throw new TriatlonException("User is not logged in.");
    }

    public synchronized List<Participant> getParticipantiProba(int pid, ITriatlonObserver client) {
        return repoPart.getParticipantiProba(pid);
    }

    public synchronized List<RaportDTO> getRaportParticipanti(int pid, ITriatlonObserver client) {
        List<RaportDTO> rez = repoPart.getRaportParticipanti(pid);
        Collections.sort(rez, Comparator.comparing(RaportDTO::getPunctaj));
        Collections.reverse(rez);
        return rez;
    }

    public synchronized void addPunctaj(PunctajDTO dto, ITriatlonObserver client) throws TriatlonException {
        boolean result = repoProba.addResult(dto.getIdproba(), dto.getIdpart(), dto.getScor());

        if (result) {
            ExecutorService executor = Executors.newFixedThreadPool(5);
            for (Integer key : loggedClients.keySet()) {
                ITriatlonObserver receiver = loggedClients.get(key);
                executor.execute(() -> {
                    try {
                        receiver.updateReceived();
                    } catch (TriatlonException e) {
                        System.out.println("Error notifying referee.");
                    }
                });
            }
        }
        else throw new TriatlonException("Participantul are deja un punctaj la aceasta proba.");
    }
}
