package matteo.blinkm.gui;

import processing.core.PApplet;
import processing.net.Client;
import processing.net.Server;

public class ProcessingSimulatorServer {

	private final PApplet applet;
	private final int port;
	private Server server;

	public ProcessingSimulatorServer(PApplet applet, int port) {
		this.applet = applet;
		this.port = port;
	}
	
	public void open() {
		server = new Server(applet, port);
	}
	
	public char[] receiveData() {
		Client client = server.available();
		if (client != null) {
			char[] rcv = new char[client.available()];
			int i = 0;
			while (client.available()>0 && i<rcv.length) {
				rcv[i++] = client.readChar();
			}
			return rcv;
		}
		return null;
	}
}
