package stoneageserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static stoneageserver.StoneAgeServer.SYNC_PORT;

public class DatabaseHandler extends UnicastRemoteObject implements DatabaseInterface {

    private Database database;
    private ArrayList<DatabaseInterface> databaseList;

    public DatabaseHandler(Database database) throws RemoteException {
        this.database = database;
        this.databaseList = new ArrayList<DatabaseInterface>();
    }

    public void addDatabase(DatabaseInterface database) {
        databaseList.add(database);
    }

    public int getSyncPort() {
        return SYNC_PORT;
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

    public byte[] get(String key) throws RemoteException {
        //TODO this method should search in all stubs
        return database.get(key);
    }

    public void putAll(Map<String, byte[]> pairs) throws RemoteException {
        database.putAll(pairs);
    }

    public Map<String, byte[]> getAll(Collection<String> keys) throws RemoteException {
        //TODO this method should search in all stubs
        return database.getAll(keys);
    }

    public Database getDatabase() throws RemoteException{
        return database;
    }

    public long getDatabaseSize() throws RemoteException {
        return database.getDatabaseSize();
    }

    private void log(String s) {
        System.out.println("[Database Handler] "+s);
    }

}