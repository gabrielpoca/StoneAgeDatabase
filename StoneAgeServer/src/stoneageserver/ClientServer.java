
package stoneageserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.net.ServerSocket;
import java.net.Socket;


class Agent extends Thread {
    
    Socket socket;
    private boolean run;
    
    public Agent(Socket socket) {
        this.socket = socket;
    }
    
    public void run() {
        try {
            DataInputStream is = new DataInputStream(socket.getInputStream());
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());
            run = true;
            while(run) {
                String current = "";
                try {
                     current = is.readUTF();                   
                } catch (EOFException e) {
                    log("Client closed");
                    run = true;
                    continue;
                }
            }
            is.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void log(String s) {
        System.out.println("[Client] "+s);
    }
}

public class ClientServer extends Thread {
    
    private int port;
    private boolean run;
    
    public ClientServer(int port) {
        this.port = port;
    }
        
    public void run() {
        try {
            ServerSocket server = new ServerSocket(port);
            log("Server started on port "+port);
            run = true;
            while(run) {
                Socket socket = server.accept();
                log("Connection accepted");
                Agent agent = new Agent(socket);
                Thread agent_thread = new Thread(agent);
                agent_thread.start();
            }
            server.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void log(String s) {
        System.out.println("[Client Server] "+s);
    }
}
