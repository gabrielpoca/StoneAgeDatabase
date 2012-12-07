
package clientserver;

import stoneageserver.DatabaseHandler;
import stoneageserver.DatabaseInterface;

import static stoneageserver.StoneAgeServer.CLIENT_PORT;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ClientServer extends Thread {
    
    private DatabaseHandler databaseHandler;

    public ClientServer(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }
    
    public void run() {	
        try {
            DatabaseInterface stub = (DatabaseInterface) UnicastRemoteObject.exportObject(databaseHandler, 0);
            Registry registry = null;
            registry = LocateRegistry.createRegistry(CLIENT_PORT);
            registry.rebind("/localhost:"+CLIENT_PORT+"/connect", stub);
            log("Ready on port "+CLIENT_PORT);
            System.out.println("deu");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void log(String s) {
        System.out.println("[Client Server] "+s);
    }
    
}
