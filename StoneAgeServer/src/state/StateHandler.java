
package state;

/**
 * State is a class that stores a server state. Each peer is suposed to have each others state.
 * The stored information is the address, port and stub.
 */

import database.DatabaseHandler;
import database.DatabaseInterface;

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

    /**
     * Saves the state and database handler.
     * @param stateHandler stateHandler to be added to the lists.
     */
    public synchronized void addStateHandler(StateInterface stateHandler) throws RemoteException {
        log("Adding state and database handler...");
        this.stateHandlerList.add(stateHandler);
        this.databaseHandler.addDatabase(stateHandler.getDatabase());
        log("There are now "+stateHandlerList.size()+" remote state handlers");
    }

    /**
     * Returns the current StateHandler's database.
     * @return DatabaseInterface.
     */
    public DatabaseInterface getDatabase() throws RemoteException {
        return databaseHandler;
    }

    /**
     * Returns the local port being used.
     * @return An integer corresponding to the server port.
     */
    public int getClientPort() throws RemoteException {
        return CLIENT_PORT;
    }

    /**
     * Updates the list of remote stateHandlers and remote databases list.
     * @param list
     */
    public void setStateHandlerList(ArrayList<StateInterface> list) throws RemoteException{
        this.stateHandlerList = list;
        for(StateInterface entry : list) {
            databaseHandler.addDatabase(entry.getDatabase());
        }
    }

    /**
     * Registers a new stateHandler. Adds it to the current database. Broadcasts it to the
     * other databases and setups his state.
     * @param stateHandler
     * @throws RemoteException
     */
    public void registerNewStateHandler(StateInterface stateHandler) throws RemoteException {
        stateHandler.setStateHandlerList(this.getStateHandlerList());
        stateHandler.addStateHandler(this);
        broadcastStateHandler(stateHandler);
        addStateHandler(stateHandler);
    }


    /* CLIENT */

    /**
     * Returns the most appropriate database to a client.
     * @return DatabaseInterface object that a client should use to communicate with.
     */
    public DatabaseInterface requestDatabase() throws RemoteException {
        //TODO this method may be too slow because! It makes to many calculations!
        DatabaseInterface current = databaseHandler;
        for(StateInterface state : stateHandlerList) {
            if(state.getDatabase().size() < current.size())
                current = state.getDatabase();
        }
        return current;
    }


    /* PRIVATE */

    /**
     * Returns a list of all locally saved state handlers.
     * @return A clone of the current stateHandlerList object.
     */
    private ArrayList<StateInterface> getStateHandlerList() throws RemoteException {
        return (ArrayList<StateInterface>) stateHandlerList.clone();
    }

    /**
     * Calls the addStateHandler method in every entry of stateHandlerList.
     * The addStateHandlers adds a state handler object to its local information.
     * @param stateHandler object to send in the addStateHandler method call.
     */
    private void broadcastStateHandler(StateInterface stateHandler) {
        try {
            log("Broadcasting state handler on client port "+stateHandler.getClientPort());
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
