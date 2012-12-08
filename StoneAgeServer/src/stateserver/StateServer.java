package stateserver;

import stoneageserver.DatabaseHandler;
import stoneageserver.DatabaseInterface;
import stoneageserver.StateHandler;
import stoneageserver.StateInterface;

import java.io.*;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Iterator;

import static stoneageserver.StoneAgeServer.CLIENT_PORT;
import static stoneageserver.StoneAgeServer.SYNC_PORT;
import static stoneageserver.StoneAgeServer.MASTER;


public class StateServer extends Thread {

    private boolean run;
    private StateHandler stateHandler;


    public StateServer(StateHandler stateHandler) {
        this.stateHandler = stateHandler;
    }


    public void run() {

        if (MASTER) {
            try {
                Selector selector = Selector.open();
                ServerSocketChannel server_channel = ServerSocketChannel.open();
                server_channel.configureBlocking(false);
                server_channel.socket().bind(new InetSocketAddress("localhost", SYNC_PORT));
                server_channel.register(selector, SelectionKey.OP_ACCEPT);

                run = true;
                log("Ready on port " + SYNC_PORT);
                while (run) {
                    selector.select();
                    Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                    while (keys.hasNext() && run) {
                        SelectionKey key = (SelectionKey) keys.next();
                        keys.remove();
                        if (!key.isValid()) {
                            continue;
                        }
                        if (key.isAcceptable()) {
                            log("Connection accepted!");
                            SocketChannel channel = server_channel.accept();
                            channel.configureBlocking(false);
                            channel.register(selector, SelectionKey.OP_READ);
                        } else if (key.isReadable()) {
                            //TODO it is working like its always localhost
                            SocketChannel channel = (SocketChannel) key.channel();
                            ByteBuffer b = ByteBuffer.allocate(10000);
                            channel.read(b);
                            b.rewind();
                            int received_port = b.getInt();
                            b.clear();
                            log("Received client on port "+received_port);
                            channel.close();
                            broadCastStateServer(received_port);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            registerToMaster();
        }

    }

    /**
     * Fetch a database stub from a peer server and adds it to the database.
     * @param port Port to lookup registry
     */
    private void broadCastStateServer(int port) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", port);
            StateInterface stateHandler = (StateInterface) registry.lookup("/localhost:"+port+"/connect");
            this.stateHandler.broadcastStateHandler(stateHandler);
            this.stateHandler.addStateHandler(stateHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Registers the client port on the master peer.
     */
    private void registerToMaster() {
        try {
            //TODO right now the master is always port 9999 and it works only on localhost
            log("Registering...");
            Socket socket = new Socket("localhost", SYNC_PORT);
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            output.writeInt(CLIENT_PORT);
            output.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void log(String s) {
        System.out.println("[State Server] " + s);
    }

}
