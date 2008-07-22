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
	
	public byte[][] lines(int addr, int repeats) {
		byte[][] lines = new byte[commands.length+1][];
		for (int i = 0; i < commands.length; i++) {		
			lines[i] = command(addr, line(i, commands[i]));
		}
		lines[lines.length-1] = command(addr, setScriptLengthAndReapeats(commands.length, repeats));
		return lines;
	}
	

}
