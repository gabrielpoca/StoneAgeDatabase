
package stoneageclient;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import clientserver.ClientStateInterface;
import stoneageserver.DatabaseInterface;


public class StoneAgeClient {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry(Integer.valueOf(2000));
            DatabaseInterface database = (DatabaseInterface) registry.lookup("/localhost/connect");
            database.put("asd", "AAAAAAND RRAAAAAAAAANDOME STRIIIIING!!!!".getBytes());
            System.out.println(new String(database.get("asd")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
