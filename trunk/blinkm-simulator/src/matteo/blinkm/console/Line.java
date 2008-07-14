package matteo.blinkm.console;

import matteo.blinkm.Definition;

public class Line {

	private char duration;
	private final Definition cmd;
	private final char[] args;

	public Line(int duration, Definition cmd, char[] args) {
		this.duration = (char) duration;
		this.cmd = cmd;
		this.args = pad(args, 3);
	}

	private char[] pad(char[] args, int len) {
		if (args.length == len)
			return args;
		char[] rt = new char[len];
		for (int i=0; i<args.length; i++) {
			rt[i] = args[i];
		}
		return rt;
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
