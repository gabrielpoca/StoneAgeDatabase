package database;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static stoneageserver.StoneAgeServer.MASTER_DEFAULT_PORT;

public class DatabaseHandler extends UnicastRemoteObject implements DatabaseInterface {

    private Database database;
    private ArrayList<DatabaseInterface> databaseList;

    public DatabaseHandler(Database database) throws RemoteException {
        this.database = database;
        this.databaseList = new ArrayList<DatabaseInterface>();

        this.database.loadFilesFromFolder();
    }

    public void addDatabase(DatabaseInterface database) {
        databaseList.add(database);
    }

    public int getSyncPort() {
        return MASTER_DEFAULT_PORT;
    }

    public ArrayList<Integer> getAllSyncPorts() throws RemoteException {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for(DatabaseInterface entry: databaseList) {
            list.add(entry.getSyncPort());
        }
        return list;
    }

    public void put(String key, byte[] value) throws RemoteException {
        database.put(key, value);
    }

    public boolean contains(String key) throws RemoteException {
        return database.contains(key);
    }

    public byte[] get(String key) throws RemoteException, DatabaseFileException {
        if(database.contains(key))
            return database.get(key);
        for(DatabaseInterface d : databaseList) {
            if(d.contains(key))
                return d.get(key);
        }
        throw(new DatabaseFileException("File "+key+" not found!"));
    }

    public void putAll(Map<String, byte[]> pairs) throws RemoteException {
        log("Put all...");
        for(String key : pairs.keySet()) {
            this.put(key, pairs.get(key));
        }
    }

    public Map<String, byte[]> getAll(Collection<String> keys) throws RemoteException, DatabaseFileException {
        log("Get all...");
        Map<String, byte[]> result = new HashMap<String, byte[]>();
        for(String key : keys) {
            result.put(key, this.get(key));
        }
        return result;
    }

    public Database getDatabase() throws RemoteException{
        return database;
    }

    public long size() throws RemoteException {
        return database.size();
    }

    private void log(String s) {
        System.out.println("[Database Handler] "+s);
    }

}