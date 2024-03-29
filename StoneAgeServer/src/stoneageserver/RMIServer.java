
package stoneageserver;

import state.StateHandler;
import state.StateInterface;

import static stoneageserver.StoneAgeServer.CLIENT_PORT;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer extends Thread {
    
    private StateHandler stateHandler;

    public RMIServer(StateHandler stateHandler) {
        this.stateHandler = stateHandler;
    }
    
    public void run() {
        try {
            StateInterface stub = (StateInterface) UnicastRemoteObject.exportObject(stateHandler, 0);
            Registry registry = null;
            registry = LocateRegistry.createRegistry(CLIENT_PORT);
            registry.rebind("/localhost:"+CLIENT_PORT+"/connect", stub);
            log("Ready on port " + CLIENT_PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void log(String s) {
        System.out.println("[Database Server] "+s);
    }
    
}
