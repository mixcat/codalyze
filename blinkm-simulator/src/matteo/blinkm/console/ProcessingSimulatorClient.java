package matteo.blinkm.console;

import static matteo.blinkm.Definition.fadeToRGB;

import java.awt.Color;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import matteo.blinkm.graphics.Matrix;
import processing.net.*;

public class ProcessingSimulatorClient {

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
		
		Socket socket;
		try {
			socket = new Socket("localhost", 5204);
			write(rt0, socket);
			write(rt, socket);
			//Thread.sleep(10000);
			write(rt2, socket);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static char[] getCharArray(
			ArrayList<Character> installRedBlueOnASquare) {
		char[] rt = new char[installRedBlueOnASquare.size()];
		for(int i=0; i<rt.length; i++) {
			rt[i] = installRedBlueOnASquare.get(i);
		}
		return rt;
	}

	private static void write(char[] rt, Socket socket) throws IOException {
		byte[] buf = new byte[rt.length];
		for(int i=0; i<buf.length;i++)
			buf[i] = (byte)rt[i];
		System.out.println("writing buf " + buf.length);
		socket.getOutputStream().write(buf);
		socket.getOutputStream().flush();
	}
}
