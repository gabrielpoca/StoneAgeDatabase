
package stoneageclient;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import stoneageserver.DatabaseInterface;
import stoneageserver.StateDatabaseInterface;


public class StoneAgeClient {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry();
            StateDatabaseInterface server = (StateDatabaseInterface) registry.lookup("/localhost/connect");
            DatabaseInterface database = (DatabaseInterface) server.getDatabase();
            database.put("asd", "AAAAAAND RRAAAAAAAAANDOME STRIIIIING!!!!".getBytes());
            System.out.println(new String(database.get("asd")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
