package matteo.blinkm.console;

import static matteo.blinkm.Definition.setScriptLengthAndRepeats;
import static matteo.blinkm.Definition.writeScriptLine;

import java.awt.Color;
import java.util.ArrayList;

import matteo.blinkm.Definition;

public class ScriptBuilder {

	private ArrayList<LineBuilder> lines= new ArrayList<LineBuilder>();
	private char addr;
	private char[] val;
	private char repeats;
	private final Script parent;

	public ScriptBuilder(Script parent, int addr, int repeats) {
		this.repeats = (char) repeats;
		this.addr = (char) addr;
		this.parent = parent;
	}

	public LineBuilder line(int ticks, Definition cmd) {
		LineBuilder lineBuilder = new LineBuilder(this, ticks, cmd);
		lines.add(lineBuilder);
		return lineBuilder;
	}

	public ScriptBuilder val(int... values) {
		this.val = new char[values.length];
		for (int i = 0; i < values.length; i++) {
			val[i] = (char) values[i];
		}
		return this;
	}

	public void setCmd(char addr, Definition cmd, char[] val, ArrayList<Character> chars) {
		chars.add(addr);
		chars.add(cmd.getCmd());
		for (char character : val) {
			chars.add(character);
		}
	}
	
	public void setLine(char addr, int lineNo, char ticks, Definition cmd, char[] val, ArrayList<Character> chars) {
		chars.add(addr);
		chars.add(writeScriptLine.getCmd());
		chars.add((char) 0);
		chars.add((char) lineNo);
		chars.add(ticks);
		chars.add(cmd.getCmd());
		for (char character : val) {
			chars.add(character);
		}
	}
	
	public ArrayList<Character> bytes() {
		ArrayList<Character> chars = new ArrayList<Character>();
		setCmd(addr, setScriptLengthAndRepeats, new char [] { 0, (char) lines.size(), repeats }, chars);
		for (int i = 0; i<lines.size(); i++) {
			LineBuilder line = lines.get(i);
			Color color = line.val();
			// TODO: can this be HSB?
			char[] rgb= new char[] { (char) color.getRed(), (char) color.getGreen(), (char) color.getBlue() };
			setLine(addr, i, line.ticks(), line.cmd(),  rgb , chars );
		}
		setCmd(addr, Definition.playScript, new char [] { 0, 0, 0 }, chars);
		return chars;
	}

	public ScriptBuilder script(int addr, int repeats) {
		return parent.script(addr, repeats);
	}

}

