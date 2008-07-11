package matteo.blinkm.console;

import matteo.blinkm.Definition;

public class Line {

	private char duration;
	private final Definition cmd;
	private final char[] args;

	public Line(int duration, Definition cmd, char[] args) {
		this.duration = (char) duration;
		this.cmd = cmd;
		this.args = args;
		assert(this.args.length==3);
	}

	public char[] getArgs() {
		return args;
	}

	public char getDuration() {
		return duration;
	}

	public Definition getCmd() {
		return cmd;
	}

	public void setDuration(char lineDuration) {
		duration = lineDuration;
	}

}
