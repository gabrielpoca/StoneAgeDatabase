package stoneageserver;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class SyncServer extends Thread {

    private int port;
    private boolean run;
    private Database database;
    private ServerState state;

    public SyncServer(ServerState state, Database database, int port) {
        this.port = port;
        this.database = database;
        this.state = state;
    }

    public void run() {
        try {
            Selector selector = Selector.open();
            ServerSocketChannel server_channel = ServerSocketChannel.open();
            server_channel.configureBlocking(false);
            server_channel.socket().bind(new InetSocketAddress("localhost", port));
            server_channel.register(selector, SelectionKey.OP_ACCEPT);
            
            run = true;
            while(run) {
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
                        SocketChannel s = server_channel.accept();
			s.configureBlocking(false);
			server_channel.register(selector, SelectionKey.OP_WRITE);
		    } else if (key.isReadable()) {
                        log("Updating state...");
                        SocketChannel socket_channel = (SocketChannel) key.channel();
                        ObjectInputStream input = new ObjectInputStream(socket_channel.socket().getInputStream());
                        state = (ServerState) input.readObject();
                        input.close();
		    } else if (key.isWritable()) {
//                        SocketChannel socket_channel = (SocketChannel) key.channel();
//                        ObjectOutputStream output = new ObjectOutputStream(socket_channel.socket().getOutputStream());
//                        output.writeObject(state);
//                        output.close();
                    }
		}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void log(String s) {
        System.out.println("[Sync Server] "+s);
    }
    
}
