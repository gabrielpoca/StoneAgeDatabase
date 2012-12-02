package stoneageserver;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class SyncServerAgent extends Thread {

	private StateDatabase state;
	private boolean run;
	private String address;
	private int port;

	public SyncServerAgent(StateDatabase state, String address, int port) {
		this.state = state;
		this.address = address;
		this.port = port;
	}

	public void run() {
		try {
			Socket socket = new Socket(address, port);
			run = true;
			while (run) {
				ObjectOutputStream output_stream = new ObjectOutputStream(socket.getOutputStream());
				output_stream.writeObject(state);
				output_stream.close();
				state.notificationQueue();
			}
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void end() {
		run = false;
	}

}
