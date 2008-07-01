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
			.line(1, fadeToRGB).val(Color.yellow)
			.line(1, fadeToRGB).val(Color.cyan)
			.line(1, BlinkmCommandDef.fadeToRGB).val(Color.cyan)
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
		private static final int LINE_SIZE = 0;
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

		public void setCmd(char addr, BlinkmCommandDef cmd, char[] val, char[] dest, int offset) {
			dest[offset] = addr;
			dest[offset+1] =cmd.getCmd();
			int i;
			for (i = 2; i < val.length+2; i++) {
				dest[i] = val[i-2];
			}
			for (int j = i+1; j < SIZE-i; j++) {
				dest[j] = 0;
			}
		}
		
		public void setLine(char addr, int lineNo, BlinkmCommandDef cmd, char[] val, char[] dest, int offset) {
			dest[offset] = addr;
			dest[offset+1] = writeScriptLine.getCmd();
			dest[offset+2] = 0;
			dest[offset+3] = (char) lineNo;
			dest[offset+4] = cmd.getCmd();
			setCmd(addr, cmd, val, dest, offset+4);
		}
		
		public char[] bytes() {
			char[] rt = new char[2*SIZE +SIZE*lines.size()];
			setCmd(addr, cmd, val, rt, 0);
			setCmd(addr, BlinkmCommandDef.setScriptLengthAndRepeats, new char [] { (char) lines.size(), 0 }, rt, SIZE);
			for (int i = 0; i<lines.size(); i++) {
				LineBuilder line = lines.get(i);
				Color color = line.val();
				char[] rgb= new char[] { (char) color.getRed(), (char) color.getGreen(), (char) color.getBlue() };
				for (char c : rgb) {
					System.out.println((int)c +" ");
				}
				System.out.println();
				setLine(addr, i, line.cmd(), rgb , rt, 2*SIZE+(LINE_SIZE*i) );
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
