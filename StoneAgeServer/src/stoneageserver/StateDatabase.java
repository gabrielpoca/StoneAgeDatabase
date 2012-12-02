
package stoneageserver;

/**
 * State is a class that stores a server state. Each peer is suposed to have each others state.
 * The stored information is the address, port and stub.
 */

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

class State extends UnicastRemoteObject {
    private Database database;
    private String address;
    private int port;
    
    public State(String address, int port) throws RemoteException {
        this.address = address;
        this.port = port;
    }
    
    public State(String location) throws RemoteException {
        this.address = location;
    }

    public State(Database database) throws RemoteException {
        this.database = database;
    }
    
    public String getAddress() {
        return address;
    }
    
    public int getPort() {
    	return port;
    }
    
    public Database getDatabase() {
        return database;
    }

    public boolean equals(State e) {
        if(e.getAddress().equals(address) && e.getPort() == port)
            return true;
        else
            return false;
    }
}

/**
 * StateDatabase stores states. It is also used to hold the notifications threads.
 */

public class StateDatabase implements StateDatabaseInterface {
    ArrayList<State> servers;
    
    public StateDatabase() {
        servers = new ArrayList<State>();
    }

//    public synchronized void addServer(String address, int port) {
//        try {
//            servers.add(new State(address, port));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        notifyAll();
//    }

    public synchronized void addDatabase(Database database) {
        try {
            servers.add(new State(database));
        } catch (Exception e) {
            e.printStackTrace();
        }
        notifyAll();
    }
    
    public synchronized void notifyQueue() {
    	notifyAll();
    }
    
    public synchronized void notificationQueue() throws InterruptedException {
    	wait();
    }

    public DatabaseInterface getDatabase() throws RemoteException {
        System.out.println("GET DATABASE: "+servers.size()+" num");
        //TODO Calculate the return database
        for(State e: servers) {
            return e.getDatabase();
        }
        return null;
    }
}
