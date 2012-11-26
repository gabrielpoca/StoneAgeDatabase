
package stoneageserver;

import java.rmi.RemoteException;
import java.util.HashMap;

public class StoneAgeServer {
    
    Database database;
 
    
    public StoneAgeServer(Database database) {
        this.database = database;
    }

    public static void main(String[] args) throws RemoteException, InterruptedException {
        HashMap<String, Integer> ports = getPorts(args);
        Database database = new Database();
        ServerState state = new ServerState();
        
        ClientServer client_server = new ClientServer(database, ports.get("client"));
        Thread client_server_thread = new Thread(client_server);
        client_server_thread.start();
        
        SyncServer sync_server = new SyncServer(state, database, ports.get("sync"));
        Thread sync_server_thread = new Thread(sync_server);
        sync_server_thread.start();
        
        client_server_thread.interrupt();
        sync_server_thread.interrupt();
        client_server_thread.join();
        sync_server_thread.join();
    }
    
    /**
     * Receives the input arguments and returns a hashmap that
     * contains the session ports.
     * @param args Command line input argumetns.
     * @return Ports to be used by the session.
     */
    public static HashMap<String, Integer> getPorts(String args[]) {
        HashMap<String, Integer> ports = new HashMap<String, Integer>();
        if(args.length < 1) {
            ports.put("client", 2000);
            ports.put("sync", 9999);
        } else {
            ports.put("client", Integer.valueOf(args[0]));
            ports.put("sync", Integer.valueOf(args[1]));
        }
        return ports;
    }
}
