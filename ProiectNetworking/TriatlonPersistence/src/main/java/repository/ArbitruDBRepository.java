package repository;

import model.Arbitru;
import model.Proba;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Properties;

public class ArbitruDBRepository implements ArbitruRepository {

    private JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();

    public ArbitruDBRepository(Properties props) {
        logger.info("Initializing ArbitruDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public Arbitru getAccount(String user, String password) {logger.traceEntry();
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        Arbitru ar = new Arbitru(-1, "", "", null);
        try(PreparedStatement preStmt = con.prepareStatement("select * from Arbitru inner join Proba on Arbitru.pid = Proba.id where username=? and password=? ")){
            preStmt.setString(1, user);
            preStmt.setString(2, password);
            try(ResultSet result = preStmt.executeQuery()){
                while (result.next()){
                    int id = result.getInt("id");
                    String nume = result.getString("nume");
                    String prenume = result.getString("prenume");
                    int pid = result.getInt("pid");
                    String pname = result.getString(8);
                    Proba pr = new Proba(pid, pname);
                    ar.setID(id);
                    ar.setNume(nume);
                    ar.setPrenume(prenume);
                    ar.setProba(pr);
                }
            }
        }
        catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
        if(ar.getID() == -1) return null;
        else return ar;
    }


    @Override
    public void add(Arbitru elem) {

    }

    @Override
    public void delete(Arbitru elem) {

    }

    @Override
    public void update(Arbitru elem, Integer id) {

    }

    @Override
    public Arbitru findById(Integer id) {
        return null;
    }

    @Override
    public Collection<Arbitru> getAll() {
        return null;
    }
}
