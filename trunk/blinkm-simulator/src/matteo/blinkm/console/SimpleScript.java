package matteo.blinkm.console;

import static matteo.blinkm.Definition.setScriptLengthAndRepeats;
import static matteo.blinkm.Definition.writeScriptLine;

import java.awt.Color;
import java.util.ArrayList;

import matteo.blinkm.Definition;

public class SimpleScript {

	private final static int MAX_SCRIPT_LINES = 49;
	private ArrayList<Line> lines= new ArrayList<Line>();

	public SimpleScript() {
	}

	public SimpleScript line(int duration, Definition cmd, char...args) {
		lines.add(new Line(duration, cmd, args));
		assert(lines.size() <= MAX_SCRIPT_LINES);
		return this;
	}
	
	public SimpleScript line(int duration, Definition cmd, Color args) {
		return line(duration, cmd, new char[] { (char) args.getGreen(), (char) args.getRed(), (char) args.getBlue() });
	}

	public void setCmd(char addr, Definition cmd, char[] val, ArrayList<Character> chars) {
		chars.add(addr);
		chars.add(cmd.getCmd());
		for (char character : val) {
			chars.add(character);
		}
	}
	
	public void setLine(char addr, char scriptId, char lineNo, Line line, ArrayList<Character> chars) {
		chars.add(addr);
		chars.add(writeScriptLine.getCmd());
		chars.add(scriptId); 
		chars.add(lineNo);
		chars.add(line.getDuration());
		chars.add(line.getCmd().getCmd());
		for (char character : line.getArgs()) {
			chars.add(character);
		}
	}
	
	public ArrayList<Character> play(char repeats, char target) {
		return play(repeats, new char[] {target});
	}
	public ArrayList<Character> play(char repeats, char...targets) {
		char scriptId = 0;
		ArrayList<Character> chars = new ArrayList<Character>();
		for (char addr : targets) {
			setCmd(addr, setScriptLengthAndRepeats, new char [] { scriptId, (char) lines.size(), repeats }, chars);
			for (char lineNo = 0; lineNo<lines.size(); lineNo++) {
				Line line = lines.get(lineNo);
				setLine(addr, scriptId, lineNo, line , chars );
			}
			setCmd(addr, Definition.playScript, new char [] { 0, 0, 0 }, chars);
		}
		return chars;
	}

}

