
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

public class Database {
    
    HashMap<String, DatabaseFile> map;
    ArrayList<String> keys;

    private String folder;

    public Database(String folder) {
        map = new HashMap<String, DatabaseFile>();
        keys = new ArrayList<String>();
        this.folder = folder;
    }

    public void put(String key, byte[] value) {
        log("Put " + key);
        if(!map.containsKey(key)) {
            synchronized (map) {
                if(!map.containsKey(key))
                    map.put(key, new DatabaseFile(filenameWithPath(key)));
            }
        }
        DatabaseFile file = map.get(key);
        synchronized (file) {
            file.write(value);
        }
    }

    public boolean contains(String key) {
        if(!map.containsKey(key))
            return false;
        else
            return true;
    }

    public byte[] get(String key) {
        log("Get "+key);
        return map.get(key).read();
    }

    public long size()  {
        long size = 0;
        for(DatabaseFile file : map.values()) {
            size += file.size();
        }
        return size;
    }

    public void loadFilesFromFolder() {
        File folder = new File(this.folder);
        for(File file : folder.listFiles()) {
            if(file.isFile()) {
                map.put(file.getName(), new DatabaseFile(file, file.length()));
            }
        }
    }

    private File filenameWithPath(String filename) {
        return new File(folder+"/"+filename);
    }
    
    private void log(String s) {
        System.out.println("[Database] "+s);
    }
    
}
