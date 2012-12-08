
package stoneageserver;

import database.Database;
import database.DatabaseHandler;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class StoneAgeServer {

    public static int CLIENT_PORT;
    public static int MASTER_DEFAULT_PORT;
    public static boolean MASTER;
    
    public StoneAgeServer() {

    }

    public static void main(String[] args) throws RemoteException, InterruptedException {

        String folder = "database_"+CLIENT_PORT;
        setupPorts(args);

        verifyDatabaseFolder(folder);

        DatabaseHandler databaseHandler = new DatabaseHandler(new Database(folder));
        StateHandler stateHandler = new StateHandler(databaseHandler);

        DatabaseServer client_server = new DatabaseServer(stateHandler);
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

    public static void verifyDatabaseFolder(String folder) {
        File file = new File(folder);
        if(!file.exists()) {
            if(file.mkdir()) {
                log("Folder "+folder+" createad!");
            } else {
                log("Failed to create folder "+folder+"! Create it and run again!");
            }
        } else {
            log("Using folder "+folder+"!");
        }
    }

    public static void setupPorts(String args[]) {
        CLIENT_PORT = Integer.valueOf(args[0]);
        MASTER_DEFAULT_PORT = 1099;
        MASTER = Integer.valueOf(args[1]) == 1;
    }
    
    private static void log(String s) {
    	System.out.println("[Main] "+s);
    }
}
