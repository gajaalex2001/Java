package rpcprotocol;

import model.*;
import services.TriatlonException;
import services.ITriatlonServices;
import services.ITriatlonObserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.List;

public class TriatlonClientRpcWorker implements Runnable, ITriatlonObserver {
    private ITriatlonServices server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    public TriatlonClientRpcWorker(ITriatlonServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    public void updateReceived() throws TriatlonException {
        Response resp = new Response.Builder().type(ResponseType.ADDED_RESULT).build();
        System.out.println("Added result");
        try{
            sendResponse(resp);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();
    //  private static Response errorResponse=new Response.Builder().type(ResponseType.ERROR).build();
    private Response handleRequest(Request request){
        Response response=null;
        if (request.type()== RequestType.LOGIN){
            System.out.println("Login request ..."+request.type());
            ArbitruDTO dto = (ArbitruDTO) request.data();
            try {
                Arbitru arb = server.login(dto, this);
                return new Response.Builder().type(ResponseType.LOGIN).data(arb).build();
            }
            catch (TriatlonException e){
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type()== RequestType.LOGOUT){
            System.out.println("Logout request");
            Arbitru arb = (Arbitru) request.data();
            try{
                server.logout(arb, this);
                connected = false;
                return okResponse;
            }
            catch (TriatlonException e){
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if(request.type() == RequestType.GET_ALL) {
            int pid=(int)request.data();
            System.out.println("Get all request");
            try{
                List<Participant> participants = server.getParticipantiProba(pid, this);
                return new Response.Builder().type(ResponseType.GET_ALL).data(participants).build();
            }catch (TriatlonException e){
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if(request.type() == RequestType.GET_RAPORT) {
            int pid=(int)request.data();
            System.out.println("Get report request");
            try{
                List<RaportDTO> participants = server.getRaportParticipanti(pid, this);
                return new Response.Builder().type(ResponseType.GET_RAPORT).data(participants).build();
            }catch (TriatlonException e){
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if(request.type() == RequestType.ADD_RESULT) {
            PunctajDTO dto = (PunctajDTO) request.data();
            System.out.println("Adding punctaj");
            try{
                server.addPunctaj(dto, this);
                return new Response.Builder().type(ResponseType.OK).build();
            } catch (TriatlonException e){
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        return response;
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }
}
