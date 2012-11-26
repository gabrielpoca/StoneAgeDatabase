/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stoneageserver;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author gabriel
 */
public interface DatabaseInterface extends Remote {
    public void put(String key, byte[] value) throws RemoteException;
    public byte[] get(String key) throws RemoteException;
    public void putAll(Map<String,byte[]> pairs) throws RemoteException;
    public Map<String,byte[]> getAll(Collection<String> keys) throws RemoteException;
}
