package matteo.blinkm.console;

import java.awt.Color;

import matteo.blinkm.Definition;

public class Commands {

	private static final int LINE_LENGTH = 8;
	
	static public byte[] fadeToRGB(Color color) {
		byte[] cmd = new byte[4];
		cmd[0] = (byte) Definition.fadeToRGB.getCmd();
		cmd[1] = (byte) color.getRed();
		cmd[2] = (byte) color.getGreen();
		cmd[3] = (byte) color.getBlue();
		return cmd;
	}

	static public byte[] goToRGB(Color color) {
		byte[] cmd = new byte[4];
		cmd[0] = (byte) Definition.goToRGB.getCmd();
		cmd[1] = (byte) color.getRed();
		cmd[2] = (byte) color.getGreen();
		cmd[3] = (byte) color.getBlue();
		return cmd;
	}
	
	static public byte[] setAddress(int address) {
		byte[] cmd = new byte[5];
		cmd[0] = (byte)Definition.setAddress.getCmd();
		cmd[1] = (byte) address;
		cmd[2] = (byte) 0xd0;
		cmd[3] = (byte) 0x0d;
		cmd[4] = (byte) address;
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
	
	static public byte[] setBoot(int play, int scriptNo, int repeats, int fadeSpeed, int timeAdjust) {
		byte[] cmd = new byte[6];
		cmd[0] = (byte) Definition.setStartupParameters.getCmd();
		cmd[1] = (byte) play;
		cmd[2] = (byte) scriptNo;
		cmd[3] = (byte) repeats;
		cmd[4] = (byte) fadeSpeed;
		cmd[5] = (byte) timeAdjust;
		return cmd;
	}

	static public byte[] command(int addr, byte[] cmd) {
		return command((byte)addr, cmd);
	}
	
	static public byte[] command(byte addr, byte[] cmd) {
		byte cmdfull[] = new byte[1+cmd.length];
		cmdfull[0] = addr;
		for( int i=0; i<cmd.length; i++) {
			cmdfull[i+1] = cmd[i];
		}
		return cmdfull;
	}

	static public void throwIfCommandIsNotEmbeddable(byte[] cmd) {
		byte embeddedCmd =  cmd[0];
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
	
}
