package stoneageserver;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface StateDatabaseInterface extends Remote {
    public DatabaseInterface getDatabase() throws RemoteException;
}
