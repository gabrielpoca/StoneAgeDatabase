
package stoneageclient;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import stoneageserver.DatabaseInterface;
import stoneageserver.StateClientInterface;
import stoneageserver.StateInterface;


public class StoneAgeClient {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry(Integer.valueOf(1099));
            StateClientInterface state = (StateClientInterface) registry.lookup("/localhost:1099/connect");
            DatabaseInterface database = state.requestDatabase();
            database.put("asds", "NOVO!".getBytes());
            System.out.print(new String(database.get("asds")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
