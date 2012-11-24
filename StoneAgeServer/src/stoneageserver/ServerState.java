
package stoneageserver;

import java.util.HashMap;

class Entry {
    private String location;
    private int clients_count;
    private long stored_data_size;
    
    public Entry(String location) {
        this.location = location;
        clients_count = 0;
        stored_data_size = 0;
    }
    
    public Entry(String location, int clients_count, long stored_data_size) {
        this.location = location;
        this.clients_count = clients_count;
        this.stored_data_size = stored_data_size;
    }
    
    public String getLocation() {
        return location;
    }
    
    public int getClientsCount() {
        return clients_count;
    }
    
    public long getStoredDataSize() {
        return stored_data_size;
    }
}

public class ServerState {
    private HashMap<String, Entry> servers;
    
    public ServerState() {
        servers = new HashMap<String, Entry>();
    }
    
    public synchronized void addServer(String location) {
        servers.put(location, new Entry(location));
    }
}
