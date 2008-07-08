package matteo.blinkm.console;

import java.awt.Color;

import matteo.blinkm.Definition;

public class SimpleLine {
	//TODO: class Line and class Script
	private final char ticks;
	private final Definition cmd;
	private Color color;
	private final SimpleScript parent;
	
	public SimpleLine(SimpleScript parent, int ticks, Definition cmd) {
		this.parent = parent;
		this.cmd = cmd;
		this.ticks = (char) ticks;
	}

	public SimpleScript script(int addr, int repeats) {
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
	
	public SimpleLine val(Color color) {
		this.color = color;
		return this;
	}
	
	public SimpleLine line(int ticks, Definition cmd) {
		return parent.line(ticks, cmd);
	}
	
	public ScriptBuilder get() {
		return parent;
	}
}
