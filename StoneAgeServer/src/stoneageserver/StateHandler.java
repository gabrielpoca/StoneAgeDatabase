
package stoneageserver;

/**
 * State is a class that stores a server state. Each peer is suposed to have each others state.
 * The stored information is the address, port and stub.
 */

import java.rmi.RemoteException;
import java.util.ArrayList;

import static stoneageserver.StoneAgeServer.CLIENT_PORT;

/**
 * StateHandler stores states. It is also used to hold the notifications threads.
 */

public class StateHandler implements StateInterface, StateClientInterface {

    DatabaseHandler databaseHandler;
    ArrayList<StateInterface> stateHandlerList;
    
    public StateHandler(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
        stateHandlerList = new ArrayList<StateInterface>();
    }

    public synchronized void addStateHandler(StateInterface stateHandler) throws RemoteException {
        log("Adding state handler...");
        this.stateHandlerList.add(stateHandler);
        this.databaseHandler.addDatabase(stateHandler.getDatabase());
    }

    public DatabaseInterface getDatabase() throws RemoteException {
        return databaseHandler;
    }

    public DatabaseInterface requestDatabase() throws RemoteException {
        //TODO it always returns the same database
        return databaseHandler;
    }

    public void broadcastStateHandler(StateInterface stateHandler) {
        try {
            for(StateInterface entry : this.stateHandlerList) {
                entry.addStateHandler(stateHandler);
            }
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void log(String s) {
        System.out.println("[State Handler] "+s);
    }
}
