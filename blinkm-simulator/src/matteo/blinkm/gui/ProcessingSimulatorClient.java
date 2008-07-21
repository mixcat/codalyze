package matteo.blinkm.gui;

import java.io.IOException;
import java.net.Socket;

public class ProcessingSimulatorClient {

	private final String host;
	private final int port;
	private Socket socket;

	public ProcessingSimulatorClient(String host, int port) {
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
		System.out.println("writing buf " + rt.length);
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
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
