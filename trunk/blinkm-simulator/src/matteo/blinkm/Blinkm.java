package matteo.blinkm;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
	command name cmd char cmd byte # args # ret vals format
	
*/
public class Blinkm {
	private Cube cube;
	private ArrayList<BlinkmCommand> commands = new ArrayList<BlinkmCommand>(); 
	private final byte addr;
	private Color color = new Color(0,0,0);
	private Color fadeToColor = null;
	private char fadeSpeed = 1;
	private Logger log;
	private char[][] customScript = new char[49][];
	private char activeScript;
	private long currentScriptTick;
	private boolean scriptIsRunning;
	private char customScriptLength = 50;
	private char customScriptRepeats;
	private int currentScriptLine;
	
	Blinkm(byte addr, Cube cube) {
		this.addr = addr;
		this.cube = cube;
		log = Logger.getLogger(this.toString());
	}
	
	void setCmd(char[] command) {
		BlinkmCommand cmd = new BlinkmCommand(command);
		char[] p = cmd.getPayload();
		switch (cmd.getDefintion()) {
			case goToRGB:
				this.color = new Color(p[0],p[1],p[2]);
			break;
			
			case fadeToRGB:
				this.fadeToColor = new Color(p[0],p[1],p[2]);
			break;
			
			case fadeToHSB:
				this.fadeToColor = Color.getHSBColor(p[0], p[1], p[2]);
			break;
			
			case fadeToRandomRGB:
				this.fadeToColor = getRandomRGBColor(this.color, p[0], p[1], p[2]);
			break;
			
			case fadeToRandomHSB:
				this.fadeToColor = getRandomHSBColor(this.color, p[0], p[1], p[2]);
			break;
			
			case setFadeSpeed:
				this.fadeSpeed = p[0];
			break;
			
			case writeScriptLine:
				char id = p[0]; // skipped as only ediatable script is 0
				char line = p[1];
				this.customScript[line] = new char[] { p[2], p[3], p[4], p[5], p[6] };
			break;
			
			case playScript:
				this.scriptIsRunning = true;
				this.activeScript = p[0];
				this.currentScriptTick = 0L;
			break;
			
			case stopScript:
				this.scriptIsRunning = false;
			break;
			
			case setScriptLengthAndRepeats:
				this.customScriptLength = p[1];
				this.customScriptRepeats = p[2];
			break;
			
		}
	}
	
	public void draw(long drawCount) {
		long ticks = 0;
		if (this.scriptIsRunning) {
			for (int i=0; i<this.customScriptLength; i++) {
				char[] line = this.customScript[i];
				if (line==null)
					break;
				char lineTicks = line[0];
				
				// currently executing line
				if (currentScriptTick >= ticks && currentScriptTick < ticks+lineTicks  ) {
					//System.out.println("tick " + currentScriptTick + " line:" + i + " ticks:" + ticks);
					
					// first tick for this line
					if (currentScriptTick == ticks) {
						setCmd(new char[] { line[1], line[2], line[3], line[4] });
					}
					
					// last tick for this line
					if (currentScriptTick == ticks+lineTicks-1) {
						// if last command reset 
						if (i == customScriptLength-1) {
							currentScriptTick = 0;
							break;
						}
					}
					currentScriptTick++;
					
					break;
				}
				
				
				ticks += lineTicks;
			}
		}
		
		if (fadeToColor != null) {
			color = getFadeStep(color, fadeToColor, fadeSpeed);
			if ((fadeToColor.getRGB() - color.getRGB()) == 0) {
				fadeToColor = null;
			}
		}
		
		cube.draw(color);
	}
	
	/**
	 * Get one step in fade at given fade speed
	 * Fade speed is slow at 1 and instant at 255.
	 * @param from
	 * @param to
	 * @param fadeSpeed
	 * @return
	 */
	private Color getFadeStep(Color from, Color to, char fadeSpeed) {
		char red = (char) getFadeComponent(from.getRed(), to.getRed(), fadeSpeed);
		char green = (char) getFadeComponent(from.getGreen(), to.getGreen(), fadeSpeed);
		char blue = (char) getFadeComponent(from.getBlue(), to.getBlue(), fadeSpeed);
		return new Color(red, green, blue);
	}
	
	private double getFadeComponent(double from, double to, char fadeSpeed) {
		double diff = to - from;
		if (diff == 0)
			return from;
		double diffStep = diff * fadeSpeed / 255;
		double sign = diffStep/Math.abs(diffStep);
		double ceil = sign * Math.ceil(Math.abs(diffStep));
		double result = from + ceil;
		if (result < 0)
			return to;
		return result;
	}
	
	private Color getRandomRGBColor(Color base, char rangeR, char rangeG, char rangeB) {
		double red = rangeR - (Math.random() * rangeR)/2;
		double green = rangeG - (Math.random() * rangeG)/2;
		double blue = rangeB - (Math.random() * rangeB)/2;
		return new Color( (int)red, (int)green, (int)blue);
	}
	
	private Color getRandomHSBColor(Color base, char rangeH, char rangeS, char rangeB) {
		double hue = rangeH - (Math.random() * rangeH)/2;
		double saturation = rangeS - (Math.random() * rangeS)/2;
		double brightness = rangeB - (Math.random() * rangeB)/2;
		return Color.getHSBColor((int)hue, (int)saturation, (int)brightness);
	}
	
	@Override
	public String toString() {
		return "Blinkm<" +addr+ ">";
	}

	public class BlinkmCommand {
		private final BlinkmCommandDef definition;
		private final char[] payload;

		BlinkmCommand(char[] command) {
			this.definition =  BlinkmCommandDef.getByChar((char)command[0]);
			this.payload = new char[command.length-1];
			
			for(int i=0; i<payload.length; i++)
				this.payload[i] = command[i+1];
		}
		
		public char[] getPayload() {
			return payload;
		}

		public BlinkmCommandDef getDefintion() {
			return definition;
		}

	}
		
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
			return "BlinkCommandDef<" + c + "/" + super.toString()+">";
		}

		public char getCmd() {
			return c;
		}
		
	}
}
