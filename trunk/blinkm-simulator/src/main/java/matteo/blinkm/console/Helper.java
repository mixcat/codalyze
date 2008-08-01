package matteo.blinkm.console;

import static matteo.blinkm.graphics.Matrix.Direction.east;
import static matteo.blinkm.graphics.Matrix.Direction.south;

import java.util.HashMap;

import matteo.blinkm.BlinkMComm;
import matteo.blinkm.graphics.Matrix;
import matteo.blinkm.graphics.Matrix.Direction;
import matteo.blinkm.gui.CompoundConsole;
import matteo.blinkm.gui.ProcessingSimulatorClient;

public class Helper {
	private static BlinkMComm blinkMComm;
	protected static ProcessingSimulatorClient client = null;
	public static String init = "src/main/resources/console/init-console.bsh";
	private static int mode=0;
	public static int cols;
	public static int rows;
	public static byte[][] matrix = Matrix.build(rows,cols);
	
	public static byte[] all = Matrix.flatten(matrix, rows, cols);
	public static final int DEFAULT_PORT = 5204;
	private static final String DEFAULT_SERIAL_PORT = "/dev/tty.usbserial-A60049Ch";

	public static HashMap<String, byte[]> selectors = new HashMap<String, byte[]>();
	public static byte[] $(String name) {
		return selectors.get(name);
	}
	
	public static void write(byte[][] data) {
		for (byte[] line:data) {
			write(line);
		}
	}
	
	public static void mode(int mode) {
		Helper.mode = mode;
	}
	
	public static void write(byte[] data) {
		switch(mode) {
		case 2:
			client.write(data);
			writeSerial(data);
		break;
		case 1:
			client.write(data);
		break;
		case 0:
			writeSerial(data);
		}
	}
	
	public static void readSerial() {
		if (blinkMComm != null) {
			byte[] read = blinkMComm.read();
			if (read != null)
				System.out.print(new String(read));
		}
	}
	
	public static void writeSerial(byte[][] data) {
		for (byte[] line:data) {
			writeSerial(line);
		}
	}
	
	public static void writeSerial(byte[] data) {
		if (blinkMComm != null)
			blinkMComm.send(data);
	}
	
	public static int[] seq(int len) {
		int[] rt = new int[len];
		for (int i = 0; i < rt.length; rt[i] = i++);
		return rt;
	} 
	
	
	public static void connect() throws Exception {
		connectSimulator(DEFAULT_PORT);
		connectSerial(DEFAULT_SERIAL_PORT);
	}
	
	public static void connectSimulator(int port) {
		if (client != null) {
			client.disconnect();
		}
		client = new ProcessingSimulatorClient("localhost", port);
		client.connect();
	}
	
	public static void connectSerial(String port) throws Exception {
		blinkMComm = new BlinkMComm();
		try {
			blinkMComm.connect(CompoundConsole.simulator, port);
		} catch (Exception e) {
			blinkMComm = null;
			e.printStackTrace();
		}
	}
	
	public static void disconnectSerial() {
		if (blinkMComm != null)
			blinkMComm.disconnect();
		blinkMComm = null;
	}
	
	
	public static void sleep(int sleep) {
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static byte[] line(int i, int j, Direction dir, int len) {
		return Matrix.line(matrix, i, j, dir, len);
	}

	public static byte[] square(int i, int j, int len) {
		return Matrix.square(matrix, i, j, len);
	}
	
	public static byte[][] hlines() {
		byte[][] lines = new byte[rows][];
		for (int i:seq(rows)) lines[i] = line(i,0, east, cols);
		return lines;
	}
	
	public static byte[][] vlines() {
		byte[][] vlines = new byte[rows][];
		for (int i:seq(cols)) vlines[i] = line(0,i, south, rows);
		return vlines;
	}
	
	public static byte[][] shlines() {
		byte[][] lines = new byte[rows][];
		for (int i:seq(rows)) { lines[i] = (i%2==0) ? line(i,0, east, cols) : inv(line(i,0, east, cols)); };
		return lines;
	}
	
	public static byte[] inv(byte[] in) {
		byte[] rt = new byte[in.length];
		for (int i=0; i<rt.length; i++) {
			rt[i] = in[in.length-1-i];
		}
		return rt;
	}
	
	public static void dump(char[] in) {
		for (int i = 0; i < in.length; i++) {
			System.out.print((int)i + " ");
		}
		System.out.println("|");
	}
	
}
