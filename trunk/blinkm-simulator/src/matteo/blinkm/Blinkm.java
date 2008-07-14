package matteo.blinkm;

import java.awt.Color;
import java.util.ArrayList;


public class Blinkm {
	private final byte addr;
	private Color color = new Color(0,0,0);
	protected Color fadeToColor = null;
	protected char fadeSpeed = 1;
	protected char timeAdjiust = 128;
	private char[][] customScript = new char[49][];
	private char activeScript;
	private long currentScriptTick;
	private boolean scriptIsRunning;
	private int customScriptLength = 50;
	private int customScriptRepeats;
	
	public Blinkm(byte addr) {
		this.addr = addr;
	}
	
	//TODO: add multiple script support
	//TODO: add native scripts
	//TODO: dynamic logging system
	
	public void setCmd(Command cmd) {
		ArrayList<Character> p = cmd.getPayload();
		cmd.validate();
		switch (cmd.getDefintion()) {
			case goToRGB:
				this.color = new Color(p.get(0),p.get(1),p.get(2));
			break;
			
			case fadeToRGB:
				this.fadeToColor = new Color(p.get(0),p.get(1),p.get(2));
			break;
			
			case fadeToHSB:
				this.fadeToColor = Color.getHSBColor(p.get(0),p.get(1),p.get(2));
			break;
			
			case fadeToRandomRGB:
				this.fadeToColor = getRandomRGBColor(this.color, p.get(0),p.get(1),p.get(2));
			break;
			
			case fadeToRandomHSB:
				this.fadeToColor = getRandomHSBColor(this.color, p.get(0),p.get(1),p.get(2));
			break;
			
			case setFadeSpeed:
				this.fadeSpeed = p.get(0);
				if (this.fadeSpeed == 0) {
					throw new RuntimeException("fadeSpeed must be >= 0");
				}
			break;
			
			case setTimeAdjust:
				this.timeAdjiust = p.get(0);
			break;
			
			case writeScriptLine:
				char id = p.get(0); // skipped as only editable script is 0
				char line = p.get(1);
				char ticks = p.get(2);
				this.customScript[line] = new char[] { ticks, p.get(3)/*cmd*/, p.get(4), p.get(5), p.get(6) };
				this.currentScriptTick = 0;
				this.fadeToColor = null;
			break;
			
			case playScript:
				this.scriptIsRunning = true;
				this.activeScript = p.get(0);
				this.currentScriptTick = 0L;
				this.fadeToColor = null;
			break;
			
			case stopScript:
				this.scriptIsRunning = false;
				this.fadeToColor = null;
			break;
			
			case setScriptLengthAndRepeats:
				this.customScriptLength = p.get(1);
				this.customScriptRepeats = p.get(2);
			break;
		}
	}
	
	public Color getColor() {
		long ticks = 0;
		if (this.scriptIsRunning) {
			for (int i=0; i<this.customScriptLength; i++) {
				if (!this.scriptIsRunning)
					break;
				char[] line = this.customScript[i];
				if (line==null) //TODO: add script length control
					break;
				char lineTicks = (char) (line[0] + timeAdjiust-128);
				
				// currently executing line
				if (ticks <= currentScriptTick && currentScriptTick < ticks+lineTicks  ) {
					
					// first tick for this line
					if (currentScriptTick == ticks) {
						Definition def = Definition.getByChar(line[1]);
						setCmd(new Command(def, unpad(new char[] { line[2], line[3], line[4] }, def.getNumArgs())));
					}
					
					// if last tick for this line, reset
					if (currentScriptTick >= ticks+lineTicks-1) {
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
		
		return color;
	}
	
	private char[] unpad(char[] cs, int len) {
		if (cs.length <= len)
			return cs;
		char[] rt = new char[len];
		for (int i=0; i<rt.length; i++)
			rt[i] = cs[i];
		return rt;
	}

	/**
	 * Get one step in fade at given fade speed
	 * Fade speed is slow at 1 and instant at 255.
	 * @param from
	 * @param to
	 * @param fadeSpeed
	 * @return
	 */
	protected Color getFadeStep(Color from, Color to, char fadeSpeed) {
		char red = (char) getFadeComponent(from.getRed(), to.getRed(), fadeSpeed);
		char green = (char) getFadeComponent(from.getGreen(), to.getGreen(), fadeSpeed);
		char blue = (char) getFadeComponent(from.getBlue(), to.getBlue(), fadeSpeed);
		return new Color(red, green, blue);
	}
	
	protected double getFadeComponent(double from, double to, char fadeSpeed) {
		double diff = to - from;
		if (diff == 0)
			return from;
		double diffStep = diff / (255-fadeSpeed-1);
		double sign = diffStep/Math.abs(diffStep);
		double ceil = sign * Math.ceil(Math.abs(diffStep));
		double result = from + sign*fadeSpeed; //ceil;
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

}
