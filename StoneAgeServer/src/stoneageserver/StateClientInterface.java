package stoneageserver;


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface StateClientInterface extends Remote {
    public DatabaseInterface requestDatabase() throws RemoteException;
}
