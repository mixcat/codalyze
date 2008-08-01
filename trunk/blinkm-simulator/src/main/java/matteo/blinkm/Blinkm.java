package matteo.blinkm;

import java.awt.Color;


public class Blinkm {
	private final byte addr;
	private Color color = new Color(0,0,0);
	protected Color fadeToColor = null;
	protected byte fadeSpeed = 127;
	protected byte timeAdjiust = 0;
	protected byte[][] scripts = new byte[49][];
	protected int currentScriptTick;
	protected boolean scriptIsRunning;
	protected int customScriptLength = 50;
	private int customScriptRepeats;
	private byte currentScriptRepeat;
	private byte currentScriptStartLine;
	private int currentScriptId;
	public Blinkm(byte addr) {
		this.addr = addr;
	}
	
	public void tick() {
		long ticks = 0;
		if (this.scriptIsRunning) {
			for (int i=0; i<this.customScriptLength; i++) {
				if (!this.scriptIsRunning)
					break;
				byte[] line = this.scripts[i];
				if (line==null) //TODO: add script length control
					break;
				int lineTicks = line[0]&0xff + (timeAdjiust&0xff)-128;
				
				// currently executing line
				if (ticks <= currentScriptTick && currentScriptTick < ticks+lineTicks  ) {
					
					// first tick for this line
					if (currentScriptTick == ticks) {
						Definition def = Definition.getByChar(line[1]);
						setCmd(ByteUtils.unpad(new byte[] { line[1], line[2], line[3], line[4] }, def.getNumArgs()+1));
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
	}

	public Color getColor() {
		
		if (fadeToColor != null) {
			color = getFadeStep(color, fadeToColor, fadeSpeed);
			if ((fadeToColor.getRGB() - color.getRGB()) == 0) {
				fadeToColor = null;
			}
		}
		
		return color;
	}
	
	/**
	 * Get one step in fade at given fade speed
	 * Fade speed is slow at 1 and instant at 255.
	 * @param from
	 * @param to
	 * @param fadeSpeed
	 * @return
	 */
	protected Color getFadeStep(Color from, Color to, byte fadeSpeed) {
		char red = (char) getFadeComponent(from.getRed(), to.getRed(), fadeSpeed);
		char green = (char) getFadeComponent(from.getGreen(), to.getGreen(), fadeSpeed);
		char blue = (char) getFadeComponent(from.getBlue(), to.getBlue(), fadeSpeed);
		return new Color(red, green, blue);
	}
	
	protected static double getFadeComponent(double from, double to, byte fadeSpeed) {
		double diff = to - from;
		if (diff == 0)
			return to;
		double step = diff/fadeSpeed;
		double result = (to>=from) ? Math.ceil(to - step) : Math.floor(to-step);
		if (result < 0 || result > 255)
			throw new RuntimeException("Color is " + result + ". Should be >=0 && <= 255");
		return result;
	}
	
	private Color getRandomRGBColor(Color base, int rangeR, int rangeG, int rangeB) {
		double red = rangeR - (Math.random() * rangeR)/2;
		double green = rangeG - (Math.random() * rangeG)/2;
		double blue = rangeB - (Math.random() * rangeB)/2;
		return new Color( (int)red, (int)green, (int)blue);
	}
	
	private Color getRandomHSBColor(Color base, int rangeH, int rangeS, int rangeB) {
		double hue = rangeH - (Math.random() * rangeH)/2;
		double saturation = rangeS - (Math.random() * rangeS)/2;
		double brightness = rangeB - (Math.random() * rangeB)/2;
		return Color.getHSBColor((int)hue, (int)saturation, (int)brightness);
	}
	
	@Override
	public String toString() {
		return "Blinkm<" +addr+ ">";
	}

	public void setCmd(byte[] cmd) {
		byte defChar = cmd[0];
		Definition definition = Definition.getByChar(defChar);
		byte[] p = ByteUtils.slice(cmd, 1, definition.getNumArgs());
		switch (definition) {
		case goToRGB:
			this.color = ByteUtils.getColor(p[0], p[1], p[2]);
		break;
		
		case fadeToRGB:
			this.fadeToColor = ByteUtils.getColor(p[0], p[1], p[2]);
		break;
		
		case fadeToHSB:
			this.fadeToColor = ByteUtils.getHSBColor(p[0], p[1], p[2]);
		break;
		
		case fadeToRandomRGB:
			this.fadeToColor = getRandomRGBColor(this.color, p[0]&0xff, p[1]&0xff, p[2]&0xff);
		break;
		
		case fadeToRandomHSB:
			this.fadeToColor = getRandomHSBColor(this.color, p[0]&0xff, p[1]&0xff, p[2]&0xff);
		break;
		
		case setFadeSpeed:
			this.fadeSpeed = p[0];
			if (this.fadeSpeed == 0) {
				throw new RuntimeException("fadeSpeed must be >= 0");
			}
		break;
		
		case setTimeAdjust:
			this.timeAdjiust = p[0];
		break;
		
		case writeScriptLine:
			byte id = p[0]; // skipped as only editable script is 0
			byte line = p[1];
			byte ticks = p[2];
			this.scripts[line] = new byte[] { ticks, p[3]/*cmd*/, p[4], p[5], p[6] };
			this.currentScriptTick = 0;
			this.fadeToColor = null;
		break;
		
		case playScript:
			this.scriptIsRunning = true;
			this.currentScriptTick = 0;
			this.fadeToColor = null;
			this.currentScriptId = p[0];
			this.currentScriptRepeat = p[1];
			this.currentScriptStartLine = p[2];
		break;
		
		case stopScript:
			this.scriptIsRunning = false;
			this.fadeToColor = null;
		break;
		
		case setScriptLengthAndRepeats:
			this.customScriptLength = p[1];
			this.customScriptRepeats = p[2];
		break;
		}
	}

	private int getScriptTicks(int scriptId, byte currentScriptStartLine, int customScriptLength) {
		int count = 0;
		for (int i=currentScriptStartLine;i<customScriptLength;i++) {
			count += this.scripts[scriptId][0]&0xFF;
		}
		return count;
	}

	public Color getFadeToColor() {
		return fadeToColor;
	}

	public boolean isScriptRunning() {
		return scriptIsRunning;
	}

	public int getCustomScriptLength() {
		return customScriptLength;
	}

	public int getCustomScriptRepeats() {
		return customScriptRepeats;
	}

	public byte[][] getScript(int i) {
		return scripts;
	}

}
