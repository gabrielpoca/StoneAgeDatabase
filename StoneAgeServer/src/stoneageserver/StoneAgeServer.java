
package stoneageserver;

import databaseserver.DatabaseServer;
import stateserver.StateServer;

import java.rmi.RemoteException;

public class StoneAgeServer {

    public static int CLIENT_PORT;
    public static int SYNC_PORT;
    public static boolean MASTER;
    
    public StoneAgeServer() {

    }

    public static void main(String[] args) throws RemoteException, InterruptedException {
        setupPorts(args);

        DatabaseHandler databaseHandler = new DatabaseHandler(new Database());
        StateHandler stateHandler = new StateHandler(databaseHandler);

        DatabaseServer client_server = new DatabaseServer(stateHandler);
        Thread client_server_thread = new Thread(client_server);
        client_server_thread.start();

        // This ensures that the client is already running when the peer master connects
        Thread.sleep(1000);

        StateServer sync_server = new StateServer(stateHandler);
        Thread sync_server_thread = new Thread(sync_server);
        sync_server_thread.start();

        client_server_thread.join();
        sync_server_thread.join();
    }

    public static void setupPorts(String args[]) {
        CLIENT_PORT = Integer.valueOf(args[0]);
        SYNC_PORT = 9999;
        MASTER = Integer.valueOf(args[1]) == 1;
    }
    
    private static void log(String s) {
    	System.out.println("[Main] "+s);
    }
}
