package repository;

import model.Participant;
import model.Proba;
import model.PunctajDTO;
import model.RaportDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class ParticipantDBRepository implements ParticipantRepository {

    private JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();

    public ParticipantDBRepository(Properties props) {
        logger.info("Initializing ParticipantDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }


    @Override
    public List<Participant> getParticipantiProba(int pid) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Participant> participants = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("SELECT Participant.id, Participant.nume, Participant.prenume, pid1, pid2, pid3, scor1+scor2+scor3 as scor, p1.nume, p2.nume, p3.nume from Participant inner join Proba as p1 on Participant.pid1 = p1.id inner join Proba as p2 on Participant.pid2 = p2.id inner join Proba as p3 on Participant.pid3 = p3.id where pid1=? or pid2=? or pid3=? order by Participant.nume")){
            preStmt.setInt(1, pid);
            preStmt.setInt(2, pid);
            preStmt.setInt(3, pid);
            try(ResultSet result = preStmt.executeQuery()){
                while (result.next()){
                    int id = result.getInt("id");
                    String nume = result.getString("nume");
                    String prenume = result.getString("prenume");
                    int pid1 = result.getInt("pid1");
                    int pid2 = result.getInt("pid2");
                    int pid3 = result.getInt("pid3");
                    int scor = result.getInt("scor");
                    String pname1 = result.getString(8);
                    String pname2 = result.getString(9);
                    String pname3 = result.getString(10);
                    Proba proba1 = new Proba(pid1, pname1);
                    Proba proba2 = new Proba(pid2, pname2);
                    Proba proba3 = new Proba(pid3, pname3);
                    List<Proba> probe = new ArrayList<>();
                    probe.add(proba1);
                    probe.add(proba2);
                    probe.add(proba3);
                    Participant p = new Participant(id, nume, prenume, probe, scor);
                    participants.add(p);
                }
            }
        }
        catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
        return participants;
    }

    @Override
    public List<RaportDTO> getRaportParticipanti(int pid) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<RaportDTO> participants = new ArrayList<>(); // Select * from Participant inner join Proba as p1 on Participant.pid1 = p1.id inner join Proba as p2 on Participant.pid2 = p2.id inner join Proba as p3 on Participant.pid3 = p3.id
        try(PreparedStatement preStmt = con.prepareStatement("SELECT Participant.id, Participant.nume, Participant.prenume, pid1, pid2, pid3, scor1, scor2, scor3, p1.nume, p2.nume, p3.nume from Participant inner join Proba as p1 on Participant.pid1 = p1.id inner join Proba as p2 on Participant.pid2 = p2.id inner join Proba as p3 on Participant.pid3 = p3.id where pid1=? or pid2=? or pid3=?")){
            preStmt.setInt(1, pid);
            preStmt.setInt(2, pid);
            preStmt.setInt(3, pid);
            try(ResultSet result = preStmt.executeQuery()){
                while (result.next()){
                    int id = result.getInt("id");
                    String nume = result.getString("nume");
                    String prenume = result.getString("prenume");
                    int pid1 = result.getInt("pid1");
                    int pid2 = result.getInt("pid2");
                    int pid3 = result.getInt("pid3");
                    String pname1 = result.getString(8);
                    String pname2 = result.getString(9);
                    String pname3 = result.getString(10);
                    int scor1 = result.getInt("scor1");
                    int scor2 = result.getInt("scor2");
                    int scor3 = result.getInt("scor3");
                    Proba proba1 = new Proba(pid1, pname1);
                    Proba proba2 = new Proba(pid2, pname2);
                    Proba proba3 = new Proba(pid3, pname3);
                    List<Proba> probe = new ArrayList<>();
                    probe.add(proba1);
                    probe.add(proba2);
                    probe.add(proba3);
                    RaportDTO p;
                    if (pid1 == pid) p = new RaportDTO(id, nume, prenume, scor1);
                    else if (pid2 == pid) p = new RaportDTO(id, nume, prenume, scor2);
                    else p = new RaportDTO(id, nume, prenume, scor3);
                    participants.add(p);
                }
            }
        }
        catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
        return participants;
    }


    @Override
    public void add(Participant elem) {

    }

    @Override
    public void delete(Participant elem) {

    }

    @Override
    public void update(Participant elem, Integer id) {

    }

    @Override
    public Participant findById(Integer id) {
        return null;
    }

    @Override
    public Collection<Participant> getAll() {
        return null;
    }
}
