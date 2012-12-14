
package database;

/*
 * This class represents the local database.
 */

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Database {
    
    HashMap<String, DatabaseFile> map;

    private String folder;

    public Database(String folder) {
        map = new HashMap<String, DatabaseFile>();
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

    public void remove(String key) {
        map.get(key).delete();
        map.remove(key);
    }


    /**
     * Validates the given folder. If it doesn't exist creates it.
     */
    public void createFolder() {
        File file = new File(folder);
        if(!file.exists()) {
            if(file.mkdir()) {
                log("Folder "+folder+" created!");
            } else {
                log("Failed to create folder "+folder+"! Create it and run again!");
            }
        } else {
            log("Using folder "+folder+"!");
        }
    }

    /**
     * Loads each file in the given folder as a database entry.
     */
    public void loadFilesFromFolder() {
        File folder = new File(this.folder);
        for(File file : folder.listFiles()) {
            if(file.isFile()) {
                map.put(file.getName(), new DatabaseFile(file, file.length(), file.lastModified()));
            }
        }
    }

    public ArrayList<String> getKeys() {
        return new ArrayList<String>(map.keySet());
    }

    public void reset() {
        File folder = new File(this.folder);
        for(File file : folder.listFiles()) {
            file.delete();
        }
    }

    private File filenameWithPath(String filename) {
        return new File(folder+"/"+filename);
    }
    
    private void log(String s) {
        System.out.println("[Database] "+s);
    }
    
}
