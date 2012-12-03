package stoneageserver;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
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
                log("Slave peer register...");
                Socket socket = new Socket("localhost", 9999);
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                //TODO right the master is always port 9999
                output.writeInt(client_port);
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
//						server_channel.register(selector, SelectionKey.OP_READ);
                        key.interestOps(SelectionKey.OP_READ);
					} else if (key.isReadable()) {
						log("New peer...");
						SocketChannel socket_channel = (SocketChannel) key.channel();
						ObjectInputStream input = new ObjectInputStream(socket_channel.socket().getInputStream());
//						String host = input.readUTF();
                        int port = input.readInt();
						input.close();
                        Registry registry = LocateRegistry.getRegistry(port);
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
