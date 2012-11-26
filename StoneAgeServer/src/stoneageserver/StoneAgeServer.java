
package stoneageserver;

import java.rmi.RemoteException;

public class StoneAgeServer {

    public static void main(String[] args) throws RemoteException, InterruptedException {
        ClientServer client_server = new ClientServer(new Database());
        Thread t = new Thread(client_server);
        t.start();
        t.join();
    }
}
