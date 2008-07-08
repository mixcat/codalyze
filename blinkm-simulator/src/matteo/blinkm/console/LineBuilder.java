package matteo.blinkm.console;

import java.awt.Color;

import matteo.blinkm.Definition;

public class LineBuilder {
	//TODO: class Line and class Script
	private final char ticks;
	private final Definition cmd;
	private Color color;
	private final ScriptBuilder parent;
	
	public LineBuilder(ScriptBuilder parent, int ticks, Definition cmd) {
		this.parent = parent;
		this.cmd = cmd;
		this.ticks = (char) ticks;
	}

	public ScriptBuilder script(int addr, int repeats) {
		return parent.script(addr, repeats);
	}

	public char ticks() {
		return ticks;
	}

	public Definition cmd() {
		return cmd;
	}
	
	public Color val() {
		return color;
	}
	
	public LineBuilder val(Color color) {
		this.color = color;
		return this;
	}
	
	public LineBuilder line(int ticks, Definition cmd) {
		return parent.line(ticks, cmd);
	}
}
