package matteo.blinkm.console;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import matteo.blinkm.Definition;
import matteo.blinkm.graphics.Matrix;
import matteo.blinkm.graphics.Matrix.Direction;
import matteo.blinkm.gui.ProcessingSimulatorClient;

public class Helper {

	public static String a = "b";
	private static int host;
	
	public static void setHost(int host) {
		Helper.host = host;
	}
	
	public static ProcessingSimulatorClient client = new ProcessingSimulatorClient("localhost", 5204);
	
	public static void write(char[] data) {
		client.write(data);
	}
	
	public static void write(SimpleScript script, char[] targets) {
		write(script.play((char)0, targets));
	}
	
	public static void write(Definition cmd, char payload, char... targets) {
		write(cmd(cmd, new char[] { payload }, targets));
	}
	
	public static char[] cmd(Definition cmd, char[] payload, char[] targets) {
		return SimpleScript.cmd(cmd, payload, targets);
	}
	
	public static char[] STOP = cmd(Definition.stopScript, new char[] {}, new char[] { 0 });
	public static char[] START = cmd(Definition.playScript, new char[] { 0, 0 ,0}, new char[] { 0 });
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
		.line(255, Definition.fadeToRGB, new Color(238,130,238))
		.line(255, Definition.fadeToRGB, new Color(75, 0, 130))
		.line(255, Definition.fadeToRGB, Color.blue)
		.line(255, Definition.fadeToRGB, Color.green)
		.line(255, Definition.fadeToRGB, Color.yellow)
		.line(255, Definition.fadeToRGB, Color.orange);
	
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
		
	public static String fade1 = "for (i=0;i<5;i++) { for (j=0;j<10;j+=2) { write(cmd(setFadeSpeed, line(j,j,east,10-i*2), new char[] { 5+25*i })); } }";
}
