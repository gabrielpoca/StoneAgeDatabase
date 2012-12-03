package stoneageserver;

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

public class SyncServer extends Thread {

	private int port;
    private int client_port;
	private boolean run;
	private DatabaseHandler databaseHandler;
	private boolean master;

	public SyncServer(DatabaseHandler databaseHandler, int port, int client_port) {
        this.client_port = client_port;
		this.port = port;
		this.databaseHandler = databaseHandler;
	}
	
	public SyncServer(DatabaseHandler databaseHandler, int port, int client_port, boolean master) {
        this.client_port = client_port;
		this.port = port;
		this.databaseHandler = databaseHandler;
		this.master = master;
	}
	

	public void run() {

        if(!master) {
            try {
                log("Slave peer register on port "+client_port+"");
                Socket socket = new Socket("localhost", 9999);
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                output.writeUTF(client_port+"");
//                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                //TODO right the master is always port 9999
//                output.writeInt(client_port);
//                output.close();
//                socket.getOutputStream().write((client_port+"").getBytes());
                output.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		
		try {

			Selector selector = Selector.open();
			ServerSocketChannel server_channel = ServerSocketChannel.open();
			server_channel.configureBlocking(false);
			server_channel.socket().bind(new InetSocketAddress("localhost", port));
			server_channel.register(selector, SelectionKey.OP_ACCEPT);

			run = true;
			log("Server ready...");
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
						log("New peer...");
						SocketChannel channel = (SocketChannel) key.channel();
//						ObjectInputStream input = new ObjectInputStream(channel.socket().getInputStream());
                        //TODO it is working like its always localhost
//                        int port = input.readInt();
//						input.close();
                        ByteBuffer b = ByteBuffer.allocate(1000);
                        channel.read(b);
                        String peer_port = String.valueOf(b.asCharBuffer());
                        b.clear();
                        log("Peer port: " + peer_port);
                        Registry registry = LocateRegistry.getRegistry(Integer.valueOf(peer_port));
                        DatabaseInterface database = (DatabaseInterface) registry.lookup("/localhost/connect");
                        databaseHandler.addDatabase(database);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void log(String s) {
		System.out.println("[Sync Server] " + s);
	}

}
