
package stoneageserver;

import clientserver.ClientServer;
import java.rmi.RemoteException;
import java.util.HashMap;

public class StoneAgeServer {

    public static int CLIENT_PORT;
    public static int SYNC_PORT;
    public static boolean MASTER;

    public static final int DEFAULT_MASTER_PORT = 9999;

    
    public StoneAgeServer() {

    }

    public static void main(String[] args) throws RemoteException, InterruptedException {
        HashMap<String, Integer> ports = getPorts(args);

        DatabaseHandler databaseHandler = new DatabaseHandler(new Database());

        ClientServer client_server = new ClientServer(databaseHandler);
        Thread client_server_thread = new Thread(client_server);
        client_server_thread.start();

        // This ensures that the client is already running when the peer master connects
        Thread.sleep(1000);

        SyncServer sync_server = new SyncServer(databaseHandler, ports.get("sync"), ports.get("client"), ports.get("master") == 1);
        Thread sync_server_thread = new Thread(sync_server);
        sync_server_thread.start();

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

        CLIENT_PORT = Integer.valueOf(args[0]);
        SYNC_PORT = Integer.valueOf(args[1]);
        MASTER = Integer.valueOf(args[2]) == 1;

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
