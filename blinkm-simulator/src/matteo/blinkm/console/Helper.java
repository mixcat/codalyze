package matteo.blinkm.console;

import static matteo.blinkm.graphics.Matrix.Direction.east;
import static matteo.blinkm.graphics.Matrix.Direction.south;

import java.awt.Color;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import matteo.blinkm.Definition;
import matteo.blinkm.graphics.Matrix;
import matteo.blinkm.graphics.Matrix.Direction;
import matteo.blinkm.gui.ProcessingSimulatorClient;

public class Helper {

	private static ProcessingSimulatorClient client = null;
	
	public static void write(char[] data) {
		client.write(data);
	}
	
	
	public static final int DEFAULT_PORT = 5204;
	public static void connect() {
		connect(DEFAULT_PORT);
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
	
	public static void main(String[] args) {
		help();
	}
	
	@Console("connect to server at given port")
	public static void connect(int port) {
		if (client != null) {
			client.disconnect();
		}
		client = new ProcessingSimulatorClient("localhost", port);
		client.connect();
	}
	
	public static void write(SimpleScript script, char[] targets) {
		write(script.play((char)0, targets));
	}
	public static void write(SimpleScript script, char target) {
		write(script.play((char)0, new char[] {target}));
	}
	public static void write(SimpleScript script, int target) {
		write(script, (char)target);
	}
	public static void write(Definition cmd, char[] payload, char[] targets) {
		write(cmd(cmd, payload, targets));
	}
	public static void write(Definition cmd, char payload, char[] targets) {
		write(cmd(cmd, payload, targets));
	}
	public static void write(Definition cmd, char[] payload, char target) {
		write(cmd, payload, target );
	}
	public static void write(Definition cmd, char[] payload, int target) {
		write(cmd(cmd, payload, target));
	}
	public static void write(Definition cmd, char payload, char target) {
		write(cmd, payload, target );
	}
	
	public static void write(Definition cmd, char[] target) {
		write(cmd(cmd, target ));
	}
	public static void write(Definition cmd, char target) {
		write(cmd(cmd, target ));
	}
	public static void write(Definition cmd, int target) {
		write(cmd(cmd, target));
	}
	public static void write(Definition cmd, Color payload, char[] targets) {
		write(cmd(cmd, payload, targets));
	}
	public static void write(Definition cmd, Color payload, char target) {
		write(cmd(cmd, payload, target));
	}
	public static void write(Definition cmd, Color payload, int target) {
		write(cmd(cmd, payload, target));
	}
	
	public static char[] cmd(Definition cmd, char[] payload, char[] targets) {
		return SimpleScript.cmd(cmd, payload, targets);
	}
	public static char[] cmd(Definition cmd, char[] payload, char target) {
		return cmd(cmd, payload, new char[] { target });
	}
	public static char[] cmd(Definition cmd, char[] payload, int target) {
		return cmd(cmd, payload, new char[] { (char)target });
	}
	public static char[] cmd(Definition cmd, char payload, char[] targets) {
		return cmd(cmd, new char[] { payload }, targets );
	}
	public static char[] cmd(Definition cmd, char payload, char target) {
		return cmd(cmd, new char[] { payload }, new char[] { target });
	}
	public static char[] cmd(Definition cmd, char payload, int target) {
		return cmd(cmd, new char[] { payload }, (char) target );
	}
	public static char[] cmd(Definition cmd, int target) {
		return cmd(cmd, new char[] {}, (char) target );
	}
	public static char[] cmd(Definition cmd, char[] targets) {
		return cmd(cmd, new char[] {} , targets );
	}
	public static char[] cmd(Definition cmd, Color payload, char[] targets) {
		return cmd(cmd, new char[] {(char) payload.getRed(), (char) payload.getGreen(), (char) payload.getBlue() }, targets);
	}
	public static char[] cmd(Definition cmd, Color payload, char target) {
		return cmd(cmd, new char[] {(char) payload.getRed(), (char) payload.getGreen(), (char) payload.getBlue() }, target);
	}
	public static char[] cmd(Definition cmd, Color payload, int target) {
		return cmd(cmd, new char[] {(char) payload.getRed(), (char) payload.getGreen(), (char) payload.getBlue() }, target);
	}
	
	public static char[] STOP = cmd(Definition.stopScript,  0 );
	public static char[] START = cmd(Definition.playScript, new char[] { 0, 0 ,0},  0 );
	static int cols = 10;
	static int rows = 10;
	public static char[][] matrix = Matrix.build(rows,cols);
	
	public static char[] all = Matrix.flatten(matrix, 10, 10);
	
	public static char[] line(int i, int j, Direction dir, int len) {
		return Matrix.line(matrix, i, j, dir, len);
	}
	public static SimpleScript gb = new SimpleScript()
		.line(255, Definition.fadeToRGB, Color.green)
		.line(255, Definition.fadeToRGB, Color.blue);
	
	public static SimpleScript rainbow = new SimpleScript()
		.line(255, Definition.fadeToRGB, Color.red)
		.line(255, Definition.fadeToRGB, Color.orange)
		.line(255, Definition.fadeToRGB, Color.yellow)
		.line(255, Definition.fadeToRGB, Color.green)
		.line(255, Definition.fadeToRGB, Color.blue)
		.line(255, Definition.fadeToRGB, new Color(75, 0, 130))
		.line(255, Definition.fadeToRGB, new Color(238,130,238));
	
	public static char[] square(int i, int j, int len) {
		return Matrix.square(matrix, i, j, len);
	}
	
	public static char[] fadeGradient(int step, char[] targets) {
		ArrayList<Character> chars = new ArrayList<Character>();
		int count = 0;
		for (char addr : targets) {
			SimpleScript.setCmd(addr, Definition.setFadeSpeed, new char[] { (char) (count) }, chars);
			count+=step;
		}
		return SimpleScript.getCharArray(chars);
	}
	
	public static void testSouthLine() {
		write(rainbow, line(0,0,south,10));
	}
	
	public static HashMap<String, String> commands = new HashMap<String, String>();
	public static void record(String name, String cmd) {
		commands.put(name, cmd);
	}
	
	public static String load(String name) {
		return commands.get(name);
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
		
	public static String fade1 = "for (i=0;i<5;i++) { for (j=0;j<10;j+=2) { write(cmd(setFadeSpeed, line(j,j,east,10-i*2), new char[] { 5+25*i })); } }";
}
