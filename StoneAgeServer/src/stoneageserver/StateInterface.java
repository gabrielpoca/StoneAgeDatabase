package stoneageserver;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface StateInterface extends Remote {

    public void addStateHandler(StateInterface stateHandler) throws RemoteException;
    public DatabaseInterface getDatabase() throws RemoteException;
    public int getClientPort() throws RemoteException;
    public void setStateHandlerList(ArrayList<StateInterface> stateHandlerList) throws RemoteException;
    public void registerNewStateHandler(StateInterface stateHandler) throws RemoteException;

}
