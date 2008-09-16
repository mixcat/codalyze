package matteo.blinkm.gui;

import java.io.IOException;
import java.net.Socket;

public class TcpClient {

	private final String host;
	private final int port;
	private Socket socket;

	public TcpClient (String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public void connect() {
		try {
			socket = new Socket(host, port);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void write(byte[] rt) {
		if (socket == null) 
			return;
		try {
			socket.getOutputStream().write(rt);
			socket.getOutputStream().flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void disconnect() {
		try {
			socket.close();
			socket = null;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
