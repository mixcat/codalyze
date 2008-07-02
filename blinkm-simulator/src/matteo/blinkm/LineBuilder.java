package matteo.blinkm;

import java.awt.Color;

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

	public ScriptBuilder script(int addr, int repeats) {
		return parent.script(addr, repeats);
	}

	public char ticks() {
		return ticks;
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
