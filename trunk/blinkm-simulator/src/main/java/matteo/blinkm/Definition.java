package matteo.blinkm;

public enum Definition {
	
	goToRGB ('n',3,0, true),
	fadeToRGB ('c',3, 0, true),
	fadeToHSB ('h',3, 0, true),
	fadeToRandomRGB ('C',3, 0, true),
	fadeToRandomHSB ('H',3, 0, true),
	playScript ('p',3, 0, true),
	stopScript ('o',0, 0, false),
	setFadeSpeed ('f',1, 0, true),
	setTimeAdjust ('t',1, 0, true),
	getRGB ('g',0, 3, false),
	writeScriptLine ('W',7, 0, false),
	readScriptLine ('R',2, 5, false),
	setScriptLengthAndRepeats ('L',3, 0, false),
	setAddress ('A',4, 0, false),
	getAddress ('a',0, 1, false),
	getFirmwareVersion ('Z', 0, 1, false),
	setStartupParameters ('B', 5, 0, false);
	
	int numArgs;
	int numReturnValues;
	private final char c;
	private final boolean isValidForScript;
	
	Definition(char c, int numArgs, int numRetValues, boolean validForScript) {
		this.c = c;
		this.numArgs = numArgs;
		this.numReturnValues = numRetValues;
		this.isValidForScript = validForScript;
	}
	
	public static Definition getByChar(byte b) {
		for (Definition def : Definition.values()) {
			if (def.is(b)) {
				return def;
			}
		}
		return null;
	}
	
	public boolean is(byte c) {
		return this.c == c;
	}

	public int getNumArgs() {
		return numArgs;
	}
	
	@Override
	public String toString() {
		return "cmd<" + c + "/" + super.toString()+">";
	}

	public char getCmd() {
		return c;
	}

	public void validate(char[] payload) {
		if (payload.length != numArgs) {
			throw new RuntimeException(this + ": payload is " + payload.length +" chars. Must be " + numArgs );
		}	
	}

	public boolean isValidForScript(byte cmd) {
		return isValidForScript;
	}
	
	
}

