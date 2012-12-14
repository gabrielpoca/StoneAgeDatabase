/*
 * This interface is for the client (or peer) to know which methods to call in Database.
 * This is part of the requirements interface.
 */
package database;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Map;

public interface DatabaseInterface extends Remote {

    public void put(String key, byte[] value) throws RemoteException;
    public boolean contains(String key) throws RemoteException;
    public byte[] get(String key) throws RemoteException, DatabaseFileException;
    public void putAll(Map<String,byte[]> pairs) throws RemoteException;
    public Map<String,byte[]> getAll(Collection<String> keys) throws RemoteException, DatabaseFileException;
    
    public long size() throws RemoteException;
    public Database getDatabase() throws RemoteException;

    public int getPort() throws RemoteException;
    public void resetDatabase() throws RemoteException;
}
