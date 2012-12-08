package stoneageserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface StateInterface extends Remote {

    public void addStateHandler(StateInterface stateHandler) throws RemoteException;
    public DatabaseInterface getDatabase() throws RemoteException;
    public void broadcastStateHandler(StateInterface stateHandler) throws RemoteException;

}
