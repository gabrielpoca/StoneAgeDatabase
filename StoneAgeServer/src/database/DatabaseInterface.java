/*
 * This interface is for the client (or peer) to know which methods to call in Database.
 * This is part of the requirements interface.
 */
package database;

import database.Database;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Map;

public interface DatabaseInterface extends Remote {

    public void put(String key, byte[] value) throws RemoteException;
    public byte[] get(String key) throws RemoteException, Exception;
    public void putAll(Map<String,byte[]> pairs) throws RemoteException;
    public Map<String,byte[]> getAll(Collection<String> keys) throws RemoteException;
    
    public long getDatabaseSize() throws RemoteException;
    public Database getDatabase() throws RemoteException;
    public int getSyncPort() throws RemoteException;
}
