package matteo.blinkm;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import matteo.blinkm.console.LineSelector;
import matteo.blinkm.console.Script;
import matteo.blinkm.console.LineSelector.Direction;
import matteo.blinkm.graphics.Matrix;

import static matteo.blinkm.Definition.*;
public class MemoryCommunicationHelper {


	private char[] data;

	public MemoryCommunicationHelper() {
//		this.data = new char[] {
//				0, 'n', 255, 255, 255,  0, 0, 0, 0,
//				1, 'n', 20,  50,  10,   0, 0, 0, 0,
//				1, 'c', 255, 255, 127,  0, 0, 0, 0,
//				1, 'f', 1,   0,   0,    0, 0, 0, 0,
//				2, 'n', 255, 255, 255,  0, 0, 0, 0,
//				2, 'c', 20,  50,  10,   0, 0, 0, 0,
//				2, 'f', 2,   0,   0,    0, 0, 0, 0,
//				3, 'C', 255, 255, 255,  0, 0, 0, 0,
//
//				id, 'W', 0, 0, 60, 'c', 0, 0, 0,
//				id, 'W', 0, 1, 60, 'c', 128, 50, 200,
//				id, 'W', 0, 2, 60, 'c', 200, 128, 50,
//				id, 'L', 0, 3, 0,    0,  0,   0,   0	
//				0, 'p', 0, 0, 0,    0,  0,   0,   0
//		};
		
		int cols = 10;
		int rows = 10;
		char[][] matrix = Matrix.build(rows,cols);
		int len = 2;
		Script lbuilder = new Script();
		for (char c : LineSelector.select(matrix, 9, 9, Direction.north, len)) {
			int ticks = 25;
			lbuilder.script(0, c)
				.line(ticks, fadeToRGB).val(Color.red)
				.line(ticks, fadeToRGB).val(Color.orange)
				.line(ticks, fadeToRGB).val(Color.yellow)
				.line(ticks, fadeToRGB).val(Color.green)
				.line(ticks, fadeToRGB).val(Color.blue)
				.line(ticks, fadeToRGB).val(new Color(75,0,130))
				.line(ticks, fadeToRGB).val(new Color(238,130,238));
		}
		
		for (char c : LineSelector.select(matrix, 7, 9, Direction.east, len)) {
			int ticks = 50;
			lbuilder.script(0, c)
				.line(ticks, fadeToRGB).val(Color.red)
				.line(ticks, fadeToRGB).val(Color.orange)
				.line(ticks, fadeToRGB).val(Color.yellow)
				.line(ticks, fadeToRGB).val(Color.green)
				.line(ticks, fadeToRGB).val(Color.blue)
				.line(ticks, fadeToRGB).val(new Color(75,0,130))
				.line(ticks, fadeToRGB).val(new Color(238,130,238));
		}
		
		for (char c : LineSelector.select(matrix, 7, 7, Direction.east, len)) {
			int ticks = 100;
			lbuilder.script(0, c)
				.line(ticks, fadeToRGB).val(Color.red)
				.line(ticks, fadeToRGB).val(Color.orange)
				.line(ticks, fadeToRGB).val(Color.yellow)
				.line(ticks, fadeToRGB).val(Color.green)
				.line(ticks, fadeToRGB).val(Color.blue)
				.line(ticks, fadeToRGB).val(new Color(75,0,130))
				.line(ticks, fadeToRGB).val(new Color(238,130,238));
		}
		
		for (char c : LineSelector.select(matrix, 7, 7, Direction.south, len)) {
			int ticks = 150;
			lbuilder.script(0, c)
				.line(ticks, fadeToRGB).val(Color.red)
				.line(ticks, fadeToRGB).val(Color.orange)
				.line(ticks, fadeToRGB).val(Color.yellow)
				.line(ticks, fadeToRGB).val(Color.green)
				.line(ticks, fadeToRGB).val(Color.blue)
				.line(ticks, fadeToRGB).val(new Color(75,0,130))
				.line(ticks, fadeToRGB).val(new Color(238,130,238));
		}
		
		ArrayList<Character> chars = lbuilder.chars();
		char[] rt = new char[chars.size()];
		for(int i=0; i<rt.length; i++) {
			rt[i] = chars.get(i);
		}
		this.data = rt;
		
		
	}

	public char[] receiveData() {
		char[] copy = data;
		data = null;
		return copy;
	}
	
}
