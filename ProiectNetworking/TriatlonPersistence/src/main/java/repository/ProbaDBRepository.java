package repository;

import model.Proba;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Properties;

public class ProbaDBRepository implements ProbaRepository {

    private JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();

    public ProbaDBRepository(Properties props) {
        logger.info("Initializing ProbaDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public boolean addResult(int idproba, int idpart, int scor) {
        logger.traceEntry("Adding score {} to participant {} for competition {}", scor, idpart, idproba);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("Update Participant Set scor1 = scor1 + ? where id = ? and pid1=? and scor1 = 0")){
            preStmt.setInt(1, scor);
            preStmt.setInt(2, idpart);
            preStmt.setInt(3, idproba);
            int result = preStmt.executeUpdate();
            if (result == 0)
                try(PreparedStatement preStmt1 = con.prepareStatement("Update Participant Set scor2 = scor2 + ? where id = ? and pid2=? and scor2 = 0")){
                preStmt1.setInt(1, scor);
                preStmt1.setInt(2, idpart);
                preStmt1.setInt(3, idproba);
                int result1 = preStmt1.executeUpdate();
                if (result1 == 0)
                    try(PreparedStatement preStmt2 = con.prepareStatement("Update Participant Set scor3 = scor3 + ? where id = ? and pid3=? and scor3 = 0")) {
                        preStmt2.setInt(1, scor);
                        preStmt2.setInt(2, idpart);
                        preStmt2.setInt(3, idproba);
                        int result2 = preStmt2.executeUpdate();
                        if (result2 == 0) return false;
                    }
                }
        }
        catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
            return false;
        }
        logger.trace("Updated 1 participant");
        logger.traceExit();
        return true;
    }


    @Override
    public void add(Integer elem) {

    }

    @Override
    public void delete(Integer elem) {

    }

    @Override
    public void update(Integer elem, Proba id) {

    }

    @Override
    public Integer findById(Proba id) {
        return null;
    }

    @Override
    public Collection<Integer> getAll() {
        return null;
    }
}
