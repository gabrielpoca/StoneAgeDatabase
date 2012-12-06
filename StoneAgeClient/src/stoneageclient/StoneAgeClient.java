
package stoneageclient;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import clientserver.ClientStateInterface;
import stoneageserver.DatabaseInterface;


public class StoneAgeClient {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry(Integer.valueOf(1098));
            DatabaseInterface database = (DatabaseInterface) registry.lookup("/localhost:1098/connect");
            database.put("asds", "NOVO!".getBytes());
            System.out.print(new String(database.get("asds")));


            Registry registry2 = LocateRegistry.getRegistry(Integer.valueOf(1099));
            DatabaseInterface database2 = (DatabaseInterface) registry2.lookup("/localhost:1099/connect");
            database2.put("asds", "NOVO!".getBytes());
            System.out.print(new String(database2.get("asds")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
