import java.io.IOException;
import java.util.Properties;

import repository.*;
import server.TriatlonServicesImpl;
import services.ITriatlonServices;
import utils.AbstractServer;
import utils.ServerException;
import utils.TriatlonRpcConcurrentServer;

public class StartRpcServer {
    private static int defaultPort=55555;
    public static void main(String[] args) {
        Properties serverProps=new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties "+e);
            return;
        }
        ArbitruRepository repoArbitru = new ArbitruDBRepository(serverProps);
        ProbaRepository repoProba = new ProbaDBRepository(serverProps);
        ParticipantRepository repoPart = new ParticipantDBRepository(serverProps);
        ITriatlonServices triatlonServerImpl = new TriatlonServicesImpl(repoArbitru, repoProba, repoPart);

        int serverPort=defaultPort;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("triatlon.server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }

        System.out.println("Starting server on port: "+serverPort);
        AbstractServer server = new TriatlonRpcConcurrentServer(serverPort, triatlonServerImpl);

        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }finally {
            try {
                server.stop();
            }catch(ServerException e){
                System.err.println("Error stopping server "+e.getMessage());
            }
        }
    }
}
