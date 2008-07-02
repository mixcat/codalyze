package matteo.blinkm;

public enum BlinkmCommandDef {
	
	goToRGB ('n',3,0),
	fadeToRGB ('c',3, 0),
	fadeToHSB ('h',3, 0),
	fadeToRandomRGB ('C',3, 0),
	fadeToRandomHSB ('H',3, 0),
	playScript ('p',3, 0),
	stopScript ('o',0, 0),
	setFadeSpeed ('f',1, 0),
	setTimeAdjust ('t',1, 0),
	getRGB ('g',0, 3),
	writeScriptLine ('W',7, 0),
	readScriptLine ('R',2, 5),
	setScriptLengthAndRepeats ('L',3, 0),
	setAddress ('A',4, 0),
	getAddress ('a',0, 1),
	getFirmwareVersion ('Z', 0, 1),
	setStartupParameters ('B', 5, 0);
	
	int numArgs;
	int numReturnValues;
	private final char c;
	
	BlinkmCommandDef(char c, int numArgs, int numRetValues) {
		this.c = c;
		this.numArgs = numArgs;
		this.numReturnValues = numRetValues;
	}
	
	static BlinkmCommandDef getByChar(char c) {
		for (BlinkmCommandDef def : BlinkmCommandDef.values()) {
			if (def.is(c)) {
				return def;
			}
		}
		return null;
	}
	
	public boolean is(char c) {
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
	
}

