
package database;

/*
 * This class is a stub to be passed as the API.
 */

import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class Database {
    
    HashMap<String, byte[]> map;
    ArrayList<String> keys;
    private String folder;

    public Database(String folder) throws RemoteException {
        map = new HashMap<String, byte[]>();
        keys = new ArrayList<String>();
        this.folder = folder;
    }

    public void put(String key, byte[] value) throws RemoteException {
        log("Put " + key);
        keys.add(key);
        writeToFile(key, value);
    }

    public byte[] get(String key) throws Exception {
        log("Get "+key);
        if(!keys.contains(key))
            throw new Exception("Key doesn't exist!");
        return readFromFile(key);
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

    private byte[] readFromFile(String filename) {
        File file = getFile(filename);
        byte[] file_content = new byte[100000];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(file_content);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file_content;
    }

    private synchronized void writeToFile(String filename, byte[] value) {
        try {
            File file = getFile(filename);
            FileOutputStream out = new FileOutputStream(file);
            out.write(value);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private File getFile(String filename) {
        return new File(folder+"/"+filename);
    }
    
    private void log(String s) {
        System.out.println("[Database] "+s);
    }
    
}
