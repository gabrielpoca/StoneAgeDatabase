/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stoneageserver;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientServer extends Thread {
    
    private Database database;
    private int port = -1;
    
    public ClientServer(Database database) {
        this.database = database;
    }
    
    public ClientServer(Database database, int port) {
        this.database = database;
        this.port = port;
    }
    
    public void run() {
        try {
            DatabaseInterface stub = (DatabaseInterface) UnicastRemoteObject.exportObject(database, 0);
            Registry registry = null;
            if(port == -1)
                registry = LocateRegistry.getRegistry(port);
            else 
                registry = LocateRegistry.getRegistry();
            registry.bind("/localhost/database", stub);
            log("Server ready...");
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
    
    private void log(String s) {
        System.out.println("[Client Server] "+s);
    }
    
}
