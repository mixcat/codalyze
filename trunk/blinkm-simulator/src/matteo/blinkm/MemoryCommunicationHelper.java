package matteo.blinkm;

import java.awt.Color;
import java.util.ArrayList;

import matteo.blinkm.Blinkm.BlinkmCommandDef;
import static matteo.blinkm.Blinkm.BlinkmCommandDef.*;
public class MemoryCommunicationHelper {


	private char[] data;

	public MemoryCommunicationHelper() {
		this.data = new char[] {
				0, 'n', 255, 255, 255,  0, 0, 0, 0,
				1, 'n', 20,  50,  10,   0, 0, 0, 0,
				1, 'c', 255, 255, 127,  0, 0, 0, 0,
				1, 'f', 1,   0,   0,    0, 0, 0, 0,
				2, 'n', 255, 255, 255,  0, 0, 0, 0,
				2, 'c', 20,  50,  10,   0, 0, 0, 0,
				2, 'f', 2,   0,   0,    0, 0, 0, 0,
				3, 'C', 255, 255, 255,  0, 0, 0, 0,

				
				0, 'p', 0, 0, 0,    0,  0,   0,   0
		};
		
		
		this.data = new ScriptBuilder(0, 0)
			.cmd(goToRGB).val(255,255,255)
			.line(100, fadeToRGB).val(Color.yellow)
			//.line(20, fadeToRGB).val(Color.cyan)
			//.line(50, BlinkmCommandDef.fadeToRGB).val(Color.cyan)
			.chars();
	}
	
	char[] scriptOne(char id) {
		return new char[] {
			id, 'W', 0, 0, 60, 'c', 0, 0, 0,
			id, 'W', 0, 1, 60, 'c', 128, 50, 200,
			id, 'W', 0, 2, 60, 'c', 200, 128, 50,
			id, 'L', 0, 3, 0,    0,  0,   0,   0	
		};
	}

	public char[] receiveData() {
		char[] copy = data;
		data = null;
		return copy;
	}
	
	public class ScriptBuilder {

		private static final int SIZE = 9;
		private static final int LINE_SIZE = SIZE+4;
		private ArrayList<LineBuilder> lines= new ArrayList<LineBuilder>();
		private final char addr;
		private BlinkmCommandDef cmd;
		private int ticks;
		private char[] val;
		private final char repeats;

		public ScriptBuilder(int addr, int repeats) {
			this.repeats = (char) repeats;
			this.addr = (char) addr;
		}

		public LineBuilder line(int ticks, BlinkmCommandDef cmd) {
			LineBuilder lineBuilder = new LineBuilder(this, ticks, cmd);
			lines.add(lineBuilder);
			return lineBuilder;
		}

		public ScriptBuilder cmd(BlinkmCommandDef cmd) {
			this.cmd = cmd;
			return this;
		}
		
		public ScriptBuilder val(int... values) {
			this.val = new char[values.length];
			for (int i = 0; i < values.length; i++) {
				val[i] = (char) values[i];
			}
			return this;
		}

		public int setCmd(char addr, BlinkmCommandDef cmd, char[] val, char[] dest, int offset) {
			int start = offset; 
			dest[offset] = addr;
			dest[++offset] = cmd.getCmd();
			int i;
			for (i = ++offset; i < val.length+offset; i++) {
				dest[i] = val[i-offset];
			}
			for (int j = i; j < start+SIZE ; j++) {
				dest[j] = 'x';
			}
			return start+SIZE;
			
		}
		
		public int setLine(char addr, int lineNo, BlinkmCommandDef cmd, char[] val, char[] dest, int offset) {
			dest[offset] = addr;
			dest[++offset] = writeScriptLine.getCmd();
			dest[++offset] = 0;
			dest[++offset] = (char) lineNo;
			dest[++offset] = cmd.getCmd();
			offset = setCmd(addr, cmd, val, dest, offset);
			return offset;
		}
		
		public char[] bytes() {
			char[] rt = new char[2*SIZE +LINE_SIZE*lines.size()];
			for (int i=0; i<rt.length; i++)
				rt[i] = '*';
			int offset = setCmd(addr, cmd, val, rt, 0);
			offset = setCmd(addr, BlinkmCommandDef.setScriptLengthAndRepeats, new char [] { (char) lines.size(), 0 }, rt, offset);
			for (int i = 0; i<lines.size(); i++) {
				LineBuilder line = lines.get(i);
				Color color = line.val();
				char[] rgb= new char[] { (char) color.getRed(), (char) color.getGreen(), (char) color.getBlue() };
				for (char c : rgb) {
					System.out.println((int)c +" ");
				}
				System.out.println();
				offset = setLine(addr, i, line.cmd(), rgb , rt, offset );
			}
			return rt;
		}
	}
	
	public class LineBuilder {

		private final char ticks;
		private final BlinkmCommandDef cmd;
		private Color color;
		private final ScriptBuilder parent;
		
		public LineBuilder(ScriptBuilder parent, int ticks, BlinkmCommandDef cmd) {
			this.parent = parent;
			this.cmd = cmd;
			this.ticks = (char) ticks;
		}

		public char[] chars() {
			return parent.bytes();
			
		}

		public BlinkmCommandDef cmd() {
			return cmd;
		}
		
		public Color val() {
			return color;
		}
		
		public LineBuilder val(Color color) {
			this.color = color;
			return this;
		}
		
		public LineBuilder line(int ticks, BlinkmCommandDef cmd) {
			return parent.line(ticks, cmd);
		}
		
	}
	
}
