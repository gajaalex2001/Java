package utils;

import rpcprotocol.TriatlonClientRpcWorker;
import services.*;
import java.net.Socket;

public class TriatlonRpcConcurrentServer extends AbsConcurrentServer {
    private ITriatlonServices triatlonServer;

    public TriatlonRpcConcurrentServer(int port, ITriatlonServices triatlonServer) {
        super(port);
        this.triatlonServer = triatlonServer;
        System.out.println("Chat- TriatlonRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        TriatlonClientRpcWorker worker =new TriatlonClientRpcWorker(triatlonServer, client);

        Thread tw= new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }
}
