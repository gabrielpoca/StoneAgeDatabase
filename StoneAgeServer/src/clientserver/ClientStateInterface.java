/*
 * When requesting a database the client (or peer) should receive this object in order to ask for a database.
 */
package clientserver;

import stoneageserver.DatabaseInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientStateInterface extends Remote {
    public DatabaseInterface getDatabase() throws RemoteException;
}
