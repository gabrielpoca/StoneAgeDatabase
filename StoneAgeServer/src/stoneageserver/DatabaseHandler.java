package stoneageserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHandler implements DatabaseInterface {

    private Database database;
    private ArrayList<DatabaseInterface> databaseList;

    public DatabaseHandler(Database database) throws RemoteException {
        this.database = database;
    }

    public void addDatabase(DatabaseInterface database) {
        databaseList.add(database);
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

    public long getDatabaseSize() throws RemoteException {
        return database.getDatabaseSize();
    }

    private void log(String s) {
        System.out.println("[Database Handler] "+s);
    }

}