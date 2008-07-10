package matteo.blinkm;

import static matteo.blinkm.Definition.fadeToRGB;

import java.awt.Color;
import java.util.ArrayList;

import matteo.blinkm.console.SimpleScript;
import matteo.blinkm.graphics.Matrix;
public class MemoryCommunicationHelper {


	private char[] data;

	public MemoryCommunicationHelper() {
		int cols = 10;
		int rows = 10;
		char[][] matrix = Matrix.build(rows,cols);
		
		SimpleScript redBlueScript = new SimpleScript()
			.line(255, fadeToRGB, Color.red)
			.line(255, fadeToRGB, Color.blue);
		
		char[] square = Matrix.square(matrix, 3, 3, 2);
		ArrayList<Character> installRedBlueOnASquare = redBlueScript.script((char)0, square);
		
		char[] rt = new char[installRedBlueOnASquare.size()];
		for(int i=0; i<rt.length; i++) {
			rt[i] = installRedBlueOnASquare.get(i);
		}
		this.data = rt;
	}

	public char[] receiveData() {
		char[] copy = data;
		data = null;
		return copy;
	}
	
}

//this.data = new char[] {
//0, 'n', 255, 255, 255,  0, 0, 0, 0,
//1, 'n', 20,  50,  10,   0, 0, 0, 0,
//1, 'c', 255, 255, 127,  0, 0, 0, 0,
//1, 'f', 1,   0,   0,    0, 0, 0, 0,
//2, 'n', 255, 255, 255,  0, 0, 0, 0,
//2, 'c', 20,  50,  10,   0, 0, 0, 0,
//2, 'f', 2,   0,   0,    0, 0, 0, 0,
//3, 'C', 255, 255, 255,  0, 0, 0, 0,
//
//id, 'W', 0, 0, 60, 'c', 0, 0, 0,
//id, 'W', 0, 1, 60, 'c', 128, 50, 200,
//id, 'W', 0, 2, 60, 'c', 200, 128, 50,
//id, 'L', 0, 3, 0,    0,  0,   0,   0	
//0, 'p', 0, 0, 0,    0,  0,   0,   0
//};