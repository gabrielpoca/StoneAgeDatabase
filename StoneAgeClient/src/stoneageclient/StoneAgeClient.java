/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stoneageclient;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import stoneageserver.DatabaseInterface;

/**
 *
 * @author gabriel
 */
public class StoneAgeClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	try {
	    Registry registry = LocateRegistry.getRegistry();
	    DatabaseInterface server = (DatabaseInterface) registry.lookup("/localhost/database");
	    server.put("asd", "AAAAAAND RRAAAAAAAAANDOME STRIIIIING!!!!".getBytes());
            System.out.println(new String(server.get("asd")));
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
