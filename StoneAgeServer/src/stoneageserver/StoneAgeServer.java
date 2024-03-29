
package stoneageserver;

import database.Database;
import database.DatabaseHandler;
import state.StateHandler;
import state.StateInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class StoneAgeServer {

    public static int CLIENT_PORT;
    public static int MASTER_DEFAULT_PORT;
    public static boolean MASTER;


    public static void main(String[] args) throws RemoteException, InterruptedException {

        CLIENT_PORT = Integer.valueOf(args[0]);
        MASTER_DEFAULT_PORT = 1099;
        MASTER = Integer.valueOf(args[1]) == 1;
        String folder = "database_"+CLIENT_PORT;

        Database database = new Database(folder);
        database.createFolder();
        database.loadFilesFromFolder();

        DatabaseHandler databaseHandler = new DatabaseHandler(database);
        StateHandler stateHandler = new StateHandler(databaseHandler);

        RMIServer client_server = new RMIServer(stateHandler);
        Thread client_server_thread = new Thread(client_server);
        client_server_thread.start();

        // This ensures that the client is already running when the peer master connects
        Thread.sleep(1000);

        try {
            if (!MASTER) {
                log("Registering...");
                Registry registry = LocateRegistry.getRegistry("localhost", MASTER_DEFAULT_PORT);
                StateInterface remoteStateHandler = (StateInterface) registry.lookup("/localhost:"+ MASTER_DEFAULT_PORT +"/connect");
                remoteStateHandler.registerNewStateHandler(stateHandler);
            }
        } catch (Exception e ) {
            e.printStackTrace();
        }

        client_server_thread.join();
    }

    private static void log(String s) {
    	System.out.println("[Main] "+s);
    }
}
