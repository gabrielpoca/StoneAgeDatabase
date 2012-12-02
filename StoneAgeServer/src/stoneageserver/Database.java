
package stoneageserver;

/*
 * This class is a stub to be passed as the API.
 */

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Database {
    
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

    public long getDatabaseSize() throws RemoteException {
        long size = 0;
        for(byte[] entry : map.values()) {
            size += entry.length;
        }
        return size;
    }
    
    private void log(String s) {
        System.out.println("[Database] "+s);
    }
    
}
