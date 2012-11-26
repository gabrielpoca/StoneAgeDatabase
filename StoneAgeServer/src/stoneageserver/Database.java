/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stoneageserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author gabriel
 */
public class Database implements DatabaseInterface {
    
    HashMap<String, byte[]> map;
    
    public Database() throws RemoteException {
        map = new HashMap<String, byte[]>();
    }

    public void put(String key, byte[] value) throws RemoteException {
        log("put");
        map.put(key, value);
    }

    public byte[] get(String key) throws RemoteException {
        log("get");
        return map.get(key);
    }

    public void putAll(Map<String, byte[]> pairs) throws RemoteException {
        log("putall");
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Map<String, byte[]> getAll(Collection<String> keys) throws RemoteException {
        log("getall");
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private void log(String s) {
        System.out.println("[Database] "+s);
    }
    
}
