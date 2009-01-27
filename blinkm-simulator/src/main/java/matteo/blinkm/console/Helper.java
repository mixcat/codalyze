package matteo.blinkm.console;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

import matteo.blinkm.Definition;
import matteo.blinkm.graphics.Matrix;
import matteo.blinkm.graphics.Matrix.Direction;
import matteo.blinkm.gui.ProcessingSimulator;
import matteo.blinkm.gui.TcpClient;
import processing.core.PApplet;
import processing.serial.Serial;

public class Helper {
	protected static TcpClient client = null;
	public static String init = "src/main/resources/console/init-console.bsh";
	private static int mode=0;

	public static final int DEFAULT_PORT = 5204;
	private static final String DEFAULT_SERIAL_PORT = "/dev/tty.usbserial-A60049Ch";

	public static HashMap<String, byte[]> selectors = new HashMap<String, byte[]>();
	private static Serial serial;
	private final PApplet papplet;
	private final int cols;
	private final int rows;
	public Helper(PApplet simulator, int cols, int rows) {
		this.papplet = simulator;
		this.cols = cols;
		this.rows = rows;
	}
	
	public int getCols() {
		return cols;
	}
	
	public int getRows() {
		return rows;
	}

	public void setAddress(int a) {
		byte[] cmd = {0, 'A', (byte)a, (byte)0xd0 , (byte)0x0d, (byte)a};
		writeSerial(cmd);
	}
	public static byte[] $(String name) {
		return selectors.get(name);
	}
	
	public static void mode(int mode) {
		Helper.mode = mode;
	}
	
	public void write(byte[] data) {
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
	
	public void readSerial() {
		if (serial != null && serial.available() > 0) {
			byte[] read = serial.readBytes(); 
			if (read != null)
				System.out.println(new String(read));
		}
	}
	
	public void writeSerial(byte[][] data) {
		for (byte[] line:data) {
			writeSerial(line);
		}
	}
	
	public void writeSerial(byte[] data) {
		if (serial != null)
			serial.write(data);
	}
	
	public void connect() throws Exception {
		connectSimulator(DEFAULT_PORT);
		connectSerial(DEFAULT_SERIAL_PORT);
	}
	
	public static void connectSimulator(int port) {
		if (client != null) {
			client.disconnect();
		}
		client = new TcpClient("localhost", port);
		client.connect();
	}
	
	public void connectSerial(String port) throws Exception {
		if (serial == null)
			serial = new Serial(papplet, port, 19200);
	}
	
	public static void disconnectSerial() {
		if (serial != null)
			serial.stop();
		serial = null;
	}
	
	public static void dump(char[] in) {
		for (int i = 0; i < in.length; i++) {
			System.out.print((int)i + " ");
		}
		
		try {
			FileReader fileReader = new FileReader(new File(""));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("|");
	}
	
	public static void evo(Helper H) throws Exception {
		Color[] colors = new Color[] {
			Color.black,
			Color.red,
			Color.orange,
			Color.yellow,
			Color.green,
			Color.cyan,
			Color.blue,
			Color.magenta,
			Color.pink,
			Color.lightGray
		};
		char[] buf = new char[2];
		File f = new File("/Users/dikappa/Documents/workspace/evo_sudoku/out");
		FileReader fr = new FileReader(f);
		while(fr.read(buf) != -1) {
			byte addr = (byte) buf[0];
			int col = buf[1];
			if (addr < 1 || col > 81)
				break;
			if (col < 0 || col > 9)
				break;
			Color c  = colors[col];
			H.write(Commands.command(addr, Commands.goToRGB(c)));
			Thread.sleep(10);
		}	
	}
	
}
