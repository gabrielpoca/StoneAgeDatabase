
package stoneageserver;

import clientserver.ClientServer;

import java.rmi.RemoteException;
import java.util.HashMap;

public class StoneAgeServer {

    
    public StoneAgeServer() {
    }

    public static void main(String[] args) throws RemoteException, InterruptedException {
        HashMap<String, Integer> ports = getPorts(args);
        log("Using client "+ports.get("client")+" and sync "+ports.get("sync"));

        DatabaseHandler databaseHandler = new DatabaseHandler(new Database());

        ClientServer client_server = new ClientServer(databaseHandler, ports.get("client"));
        Thread client_server_thread = new Thread(client_server);
        client_server_thread.start();
        
        SyncServer sync_server = new SyncServer(databaseHandler, ports.get("sync"), ports.get("client"), ports.get("master") == 1);
        Thread sync_server_thread = new Thread(sync_server);
        sync_server_thread.start();
        
//        client_server_thread.interrupt();
//        sync_server_thread.interrupt();
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
            ports.put("client", 1099);
            ports.put("sync", 9999);
            ports.put("master", 1);
        } else {
            ports.put("client", Integer.valueOf(args[0]));
            ports.put("sync", Integer.valueOf(args[1]));
            ports.put("master", Integer.valueOf(args[2]));
        }
        return ports;
    }
    
    private static void log(String s) {
    	System.out.println("[Main] "+s);
    }
}
