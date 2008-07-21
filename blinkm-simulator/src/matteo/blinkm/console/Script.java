package matteo.blinkm.console;

import static matteo.blinkm.console.Commands.COMMAND_LINE_LENGTH;
import static matteo.blinkm.console.Commands.command;
import static matteo.blinkm.console.Commands.line;
import static matteo.blinkm.console.Commands.setScriptLengthAndReapeats;
import static matteo.blinkm.console.Commands.throwIfCommandIsNotEmbeddable;
import matteo.blinkm.ByteUtils;

public class Script {
	protected byte[][] commands;

	public Script build(byte[]... commands) {
		for (byte[] command : commands)
			throwIfCommandIsNotEmbeddable(command);
		this.commands = commands;
		return this;
	}
	
	
	public byte[] get(int addr, int repeats) {
		byte[] lengthAndReapeats = command(addr, setScriptLengthAndReapeats(commands.length, repeats));
		byte[] rt = new byte[commands.length*COMMAND_LINE_LENGTH + lengthAndReapeats.length];
		
		// fill with stars, to ease testing and debugging
		ByteUtils.fill(rt, '*');
		
		// put every line in a command envelope
		for (int i = 0; i < commands.length; i++) {				
			byte[] lineCommand = command(addr, line(i, commands[i]));
			int base = COMMAND_LINE_LENGTH*i; 
			for (int j=0; j<lineCommand.length; j++) {
				rt[base + j] = lineCommand[j];
			}
		}
		
		// end with setting script length and repeats
		int base = COMMAND_LINE_LENGTH*commands.length;
		for (int j=0; j<lengthAndReapeats.length; j++) {
			rt[base + j] = lengthAndReapeats[j];
		}
		
		return rt;
	}
}
