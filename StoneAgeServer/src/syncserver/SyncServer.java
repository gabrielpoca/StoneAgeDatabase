package syncserver;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class SyncServer extends Thread {

    private int port;
    private boolean run;

    public SyncServer(int port) {
        this.port = port;
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
                        
		    } else if (key.isReadable()) {
                        
		    } else if (key.isWritable()) {
                        
                    }
		}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
