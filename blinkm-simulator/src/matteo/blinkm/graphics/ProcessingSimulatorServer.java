package matteo.blinkm.graphics;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ProcessingSimulatorServer implements Runnable {

	private ServerSocket serverSocket;

	ArrayList<Character> buf = new ArrayList<Character>();

	public ProcessingSimulatorServer() {
		try {
			int port = 4449;
			System.out.println(this + " opening port " + port);
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	 public Character[] receiveData() {
		Character[] rcv = buf.toArray(new Character[buf.size()]);
		if (buf.size() > 0) {
			buf = new ArrayList<Character>();
			return rcv;
		}
		return null;
	}

	public void run() {

		Socket clientSocket;
		InputStream in;
		try {
			while (true) {
			clientSocket = serverSocket.accept();
			in = clientSocket.getInputStream();
				
				if (in.available() > 0)
				buf.add((Character) (char)in.read());
			}
		} catch (IOException e) {
			System.out.println("Accept failed: 4444");
			System.exit(-1);
		}

	}
}
