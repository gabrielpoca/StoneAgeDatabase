
package clientserver;

import stoneageserver.DatabaseHandler;
import stoneageserver.DatabaseInterface;
import stoneageserver.StateDatabase;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ClientServer extends Thread {
    
    private DatabaseHandler databaseHandler;
    private int port = -1;

    public ClientServer(DatabaseHandler databaseHandler, int port) {
        this.databaseHandler = databaseHandler;
        this.port = port;
    }
    
    public void run() {	
        try {
            DatabaseInterface stub = (DatabaseInterface) UnicastRemoteObject.exportObject(databaseHandler, 0);
            Registry registry = null;
            registry = LocateRegistry.getRegistry(port);
            registry.bind("/localhost/connect", stub);
            log("Ready...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void log(String s) {
        System.out.println("[Client Server] "+s);
    }
    
}
