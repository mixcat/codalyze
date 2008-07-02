package matteo.blinkm;

import java.awt.Color;
import java.util.ArrayList;

import static matteo.blinkm.BlinkmCommandDef.*;
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
		
		
			
		Script builder = new Script();
		for (int i=0; i<100; i++) {
			int ticks = i%4*25+i%3*50+5;
			builder.script(i,1)
				.line(i, fadeToRGB).val(Color.red)
				.line(ticks, fadeToRGB).val(Color.orange)
				.line(ticks, fadeToRGB).val(Color.yellow)
				.line(ticks, fadeToRGB).val(Color.green)
				.line(ticks, fadeToRGB).val(Color.blue)
				.line(ticks, fadeToRGB).val(new Color(75,0,130))
				.line(ticks, fadeToRGB).val(new Color(238,130,238));
		}
		
		for (int i=0; i<100; i++) {
		}
		
		ArrayList<Character> chars = builder.chars();
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
