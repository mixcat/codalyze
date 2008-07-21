package matteo.blinkm.console;

import java.awt.Color;

import matteo.blinkm.Definition;

public class Commands {

	public static final byte BROADCAST = 0; // 0 is broadcast address
	public static final int LINE_LENGTH = 8;
	public static final int COMMAND_LINE_LENGTH = 12;
	public static final int COMMAND_ENVELOPE_LENGHT = 4;
	
	static public byte[] fadeToRGB(Color color) {
		byte[] cmd = new byte[4];
		cmd[0] = (byte) Definition.fadeToRGB.getCmd();
		cmd[1] = (byte) color.getRed();
		cmd[2] = (byte) color.getGreen();
		cmd[3] = (byte) color.getBlue();
		return cmd;
	}
	
	static public byte[] stopScript() {
		byte[] cmd = new byte[1];
		cmd[0] = (byte) Definition.stopScript.getCmd();
		return cmd;
	}
	
	static public byte[] setFadeSpeed(int fadeSpeed) {
		byte[] cmd = new byte[2];
		cmd[0] = (byte) Definition.setFadeSpeed.getCmd();
		cmd[1] = (byte) fadeSpeed;
		return cmd;
	}
	
	static public byte[] playScript(int scriptId, int repeat, int startLine) {
		byte[] cmd = new byte[4];
		cmd[0] = (byte) Definition.playScript.getCmd();
		cmd[1] = (byte) scriptId;
		cmd[2] = (byte) repeat;
		cmd[3] = (byte) startLine;
		return cmd;
	}
	
	static public byte[] setScriptLengthAndReapeats(int length, int repeats) {
		byte[] cmd = new byte[4];
		cmd[0] = (byte) Definition.setScriptLengthAndRepeats.getCmd();
		cmd[1] = (byte) 0; //scriptId: currently only script 0 is valid
		cmd[2] = (byte) length;
		cmd[3] = (byte) repeats;
		return cmd;
	}
	
	static public byte[] setTimeAdjust(int adjust) {
		byte[] cmd = new byte[2];
		cmd[0] = (byte) Definition.setTimeAdjust.getCmd();
		cmd[1] = (byte) adjust; 
		return cmd;
	}

	static public byte[] command(int addr, byte[] cmd) {
		return command((byte)addr, cmd);
	}
	
	static public byte[] command(byte addr, byte[] cmd) {
		byte cmdfull[] = new byte[COMMAND_ENVELOPE_LENGHT+cmd.length];
		cmdfull[0] = 0x01;
		cmdfull[1] = addr;
		cmdfull[2] = (byte)cmd.length;
		cmdfull[3] = 0x00;
		for( int i=0; i<cmd.length; i++) {
			cmdfull[COMMAND_ENVELOPE_LENGHT+i] = cmd[i];
		}
		return cmdfull;
	}

	static public void throwIfCommandIsNotEmbeddable(byte[] cmd) {
		char embeddedCmd = (char) cmd[0];
		Definition definition = Definition.getByChar(embeddedCmd);
		if (!definition.isValidForScript(embeddedCmd)) {
			throw new RuntimeException("Command [" + definition.toString()+ "] not embeddable in a script line");
		}
	}
	
	static public byte[] line(int lineNo, byte[] cmd) {
		throwIfCommandIsNotEmbeddable(cmd);
		byte[] cmdfull = new byte[LINE_LENGTH];
		cmdfull[0] = (byte)'W';   // "Write Script Line" command
		cmdfull[1] = (byte)0;     // script id (0==eeprom)
		cmdfull[2] = (byte)lineNo;     // script line number
		cmdfull[3] = (byte)255;   // duration in ticks
		for( int i=0; i<cmd.length; i++) {
			cmdfull[4+i] = cmd[i];
		}
		return cmdfull;
	}
	
	static public byte[] removeCommandEnvelope(byte[] cmd) {
		byte[] rt = new byte[cmd.length - COMMAND_ENVELOPE_LENGHT];
		for( int i=0; i<rt.length; i++) {
			rt[i] = cmd[COMMAND_ENVELOPE_LENGHT+i];
		}
		return rt;
	}
	
}
