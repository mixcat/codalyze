package matteo.blinkm.console;

import static matteo.blinkm.graphics.Matrix.Direction.east;
import static matteo.blinkm.graphics.Matrix.Direction.south;

import java.awt.Color;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import processing.serial.Serial;

import matteo.blinkm.BlinkMComm;
import matteo.blinkm.Definition;
import matteo.blinkm.graphics.Matrix;
import matteo.blinkm.graphics.Matrix.Direction;
import matteo.blinkm.gui.CompoundConsole;
import matteo.blinkm.gui.ProcessingSimulatorClient;

public class Helper {

	protected static ProcessingSimulatorClient client = null;
	
	public static void writeBoth(byte[] data) {
		writeSerial(data);
		write(data);
	}
	
	
	public static void write(byte[] data) {
		client.write(data);
	}
	
	public static void writeSerial(byte[][] data) {
		for (byte[] line:data) {
			writeSerial(line);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void writeSerial(byte[] data) {
		if (blinkMComm != null)
			blinkMComm.send(data);
	}
	
	public static final int DEFAULT_PORT = 5204;
	private static final String DEFAULT_SERIAL_PORT = "/dev/tty.usbserial-A60049Ch";
	public static void connect() throws Exception {
		connectSimulator(DEFAULT_PORT);
		connectSerial(DEFAULT_SERIAL_PORT);
	}
	
	@Console("connect to server at given port")
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
	

	static int cols = 10;
	static int rows = 10;
	public static char[][] matrix = Matrix.build(rows,cols);
	
	public static char[] all = Matrix.flatten(matrix, 10, 10);
	
	public static char[] line(int i, int j, Direction dir, int len) {
		return Matrix.line(matrix, i, j, dir, len);
	}
	
	public static Script tst =new Script().build(new byte[][] {
			Commands.fadeToRGB(Color.red),
			Commands.fadeToRGB(Color.green),
			Commands.fadeToRGB(Color.blue)}
	);
	
	public static Script rbw =new Script().build(new byte[][] {
			Commands.fadeToRGB(Color.red),
			Commands.fadeToRGB(Color.orange),
			Commands.fadeToRGB(Color.yellow),
			Commands.fadeToRGB(Color.green),
			Commands.fadeToRGB(Color.blue),
			Commands.fadeToRGB(new Color(75, 0, 130)),
			Commands.fadeToRGB(new Color(238, 130, 238))
			}
	);
	public static char[] square(int i, int j, int len) {
		return Matrix.square(matrix, i, j, len);
	}
	
	public static int[] seq(int len) {
		int[] rt = new int[len];
		for (int i = 0; i < rt.length; rt[i] = i++);
		return rt;
	} 
	
	public static char[][] hlines() {
		char[][] lines = new char[10][];
		for (int i:seq(10)) lines[i] = line(i,0, east, 10);
		return lines;
	}
	
	public static char[][] vlines() {
		char[][] vlines = new char[10][];
		for (int i:seq(10)) vlines[i] = line(0,i, south, 10);
		return vlines;
	}
	
	public static byte[] txt() {
		byte[] cmd = new byte[36];
		cmd[0] = (byte)1;
		cmd[1] = (byte)0;
		cmd[2] = (byte)8;
		cmd[3] = (byte)0;
		cmd[4] = (byte)'W';
		cmd[5] = (byte)0;
		cmd[6] = (byte)0;
		cmd[7] = (byte)255;
		cmd[8] = (byte)'c';
		cmd[9] = (byte)255;
		cmd[10] = (byte)0;
		cmd[11] = (byte)255;
		
		cmd[12] = (byte)1;
		cmd[13] = (byte)0;
		cmd[14] = (byte)8;
		cmd[15] = (byte)0;
		cmd[16] = (byte)'W';
		cmd[17] = (byte)0;
		cmd[18] = (byte)1;
		cmd[19] = (byte)255;
		cmd[20] = (byte)'c';
		cmd[21] = (byte)125;
		cmd[22] = (byte)255;
		cmd[23] = (byte)0;
		
		cmd[24] = (byte)1;
		cmd[25] = (byte)0;
		cmd[26] = (byte)8;
		cmd[27] = (byte)0;
		cmd[28] = (byte)'W';
		cmd[29] = (byte)0;
		cmd[30] = (byte)2;
		cmd[31] = (byte)255;
		cmd[32] = (byte)'c';
		cmd[33] = (byte)255;
		cmd[34] = (byte)125;
		cmd[35] = (byte)125;
		
		return cmd;
	}
	
	public static char[][] shlines() {
		char[][] lines = new char[10][];
		for (int i:seq(10)) { lines[i] = (i%2==0) ? line(i,0, east, 10) : inv(line(i,0, east, 10)); };
		return lines;
	}
	
	public static char[] inv(char[] in) {
		char[] rt = new char[in.length];
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
	
	public static void help() {
		Method[] methods = Helper.class.getMethods();
		for (Method method : methods) {
			Console annotation = method.getAnnotation(Console.class);
			if (annotation != null) {
				System.out.println(method.toGenericString() + " " + method.getName() + " " + annotation.value());
			}
		}
	}
	
	public static String fade1 = "for (i=0;i<5;i++) { for (j=0;j<10;j+=2) { write(cmd(setFadeSpeed, line(j,j,east,10-i*2), new char[] { 5+25*i })); } }";
	private static BlinkMComm blinkMComm;
}
