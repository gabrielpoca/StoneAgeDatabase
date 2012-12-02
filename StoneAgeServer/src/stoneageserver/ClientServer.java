
package stoneageserver;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ClientServer extends Thread {
    
    private StateDatabase stateDatabase;
    private int port = -1;
    
    public ClientServer(StateDatabase stateDatabase) {
        this.stateDatabase = stateDatabase;
    }
    
    public ClientServer(StateDatabase stateDatabase, int port) {
        this.stateDatabase = stateDatabase;
        this.port = port;
    }
    
    public void run() {	
        try {
            StateDatabaseInterface stub = (StateDatabaseInterface) UnicastRemoteObject.exportObject(stateDatabase, 0);
            Registry registry = null;
            if(port == -1)
                registry = LocateRegistry.getRegistry(port);
            else 
                registry = LocateRegistry.getRegistry();
            registry.bind("/localhost/connect", stub);
            log("Server ready...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void log(String s) {
        System.out.println("[Client Server] "+s);
    }
    
}
