package repository;

import model.Participant;
import model.PunctajDTO;
import model.RaportDTO;

import java.util.List;

public interface ParticipantRepository extends repository.Repository<Participant, Integer> {
    List<Participant> getParticipantiProba(int pid);
    List<RaportDTO> getRaportParticipanti(int pid);
}
