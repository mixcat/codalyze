package matteo.blinkm.console;

import static matteo.blinkm.Definition.fadeToRGB;

import java.awt.Color;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import matteo.blinkm.graphics.Matrix;
import processing.net.*;

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
	public static void main(String[] args) {
		int cols = 10;
		int rows = 10;
		char[][] matrix = Matrix.build(rows,cols);
				
		char[] rt0 = getCharArray(new SimpleScript()
		.line(25, fadeToRGB, Color.white)
		.line(25, fadeToRGB, Color.gray)
		.play((char)0, (char)0));
		
		char[] rt = getCharArray(new SimpleScript()
		.line(255, fadeToRGB, Color.green)
		.line(255, fadeToRGB, Color.blue)
		.play((char)0, Matrix.square(matrix, 1, 1, 6)));
		
		char[] rt2 = getCharArray(new SimpleScript()
		.line(150, fadeToRGB, Color.green)
		.line(200, fadeToRGB, Color.blue)
		.play((char)0, Matrix.square(matrix, 2, 2, 5)));
		
		ProcessingSimulatorClient client = new ProcessingSimulatorClient("localhost", 5204);
		client.connect();
		client.write(new SimpleScript()
		.line(25, fadeToRGB, Color.white)
		.line(25, fadeToRGB, Color.gray)
		.play((char)0, (char)0));
	}

	public static char[] getCharArray(ArrayList<Character> data) {
		char[] rt = new char[data.size()];
		for(int i=0; i<rt.length; i++) {
			rt[i] = data.get(i);
		}
		return rt;
	}
	
	public void write(ArrayList<Character> data) {
		write(getCharArray(data));
	}

	public void write(char[] rt) {
		if (socket == null) 
			return;
		byte[] buf = new byte[rt.length];
		for(int i=0; i<buf.length;i++)
			buf[i] = (byte)rt[i];
		System.out.println("writing buf " + buf.length);
		try {
			socket.getOutputStream().write(buf);
			socket.getOutputStream().flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
