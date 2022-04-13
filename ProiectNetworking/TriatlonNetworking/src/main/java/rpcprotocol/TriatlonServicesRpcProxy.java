package rpcprotocol;

import model.*;
import services.ITriatlonObserver;
import services.ITriatlonServices;
import services.TriatlonException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TriatlonServicesRpcProxy implements ITriatlonServices {
    private String host;
    private int port;

    private ITriatlonObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;
    public TriatlonServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<Response>();
    }

    @Override
    public Arbitru login(ArbitruDTO dto, ITriatlonObserver client) throws TriatlonException {
        initializeConnection();
        Request req = new Request.Builder().type(RequestType.LOGIN).data(dto).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            throw new TriatlonException(err);
        }
        this.client = client;
        Arbitru arb = (Arbitru) response.data();
        return arb;
    }

    @Override
    public void logout(Arbitru dto, ITriatlonObserver client) throws TriatlonException {
        Request req = new Request.Builder().type(RequestType.LOGOUT).data(dto).build();
        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        if(response.type() == ResponseType.ERROR){
            String err=response.data().toString();
            throw new TriatlonException(err);
        }
    }

    @Override
    public void addPunctaj(PunctajDTO dto, ITriatlonObserver client) throws TriatlonException {
        Request req = new Request.Builder().type(RequestType.ADD_RESULT).data(dto).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR){
            String err=response.data().toString();
            throw new TriatlonException(err);
        }
    }

    @Override
    public List<Participant> getParticipantiProba(int pid, ITriatlonObserver client) throws TriatlonException {
        Request req = new Request.Builder().type(RequestType.GET_ALL).data(pid).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            throw new TriatlonException(err);
        }
        List<Participant> participants = (List<Participant>) response.data();
        return participants;
    }

    @Override
    public List<RaportDTO> getRaportParticipanti(int pid, ITriatlonObserver client) throws TriatlonException {
        Request req = new Request.Builder().type(RequestType.GET_RAPORT).data(pid).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            throw new TriatlonException(err);
        }
        List<RaportDTO> participants = (List<RaportDTO>) response.data();
        return participants;
    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(Request request)throws TriatlonException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new TriatlonException("Error sending object "+e);
        }
    }

    private Response readResponse() throws TriatlonException {
        Response response=null;
        try{

            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() throws TriatlonException {
        try {
            connection=new Socket(host,port);
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdate(Response response){
        if (response.type() == ResponseType.ADDED_RESULT){
            System.out.println("Proxy added result");
            try{
                client.updateReceived();
            } catch (TriatlonException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isUpdate(Response response){
        return response.type()== ResponseType.ADDED_RESULT;
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    System.out.println("response received "+response);
                    if (isUpdate((Response)response)){
                        handleUpdate((Response)response);
                    }else{

                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }
}
