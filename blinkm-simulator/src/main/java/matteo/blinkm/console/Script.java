package matteo.blinkm.console;

import static matteo.blinkm.console.Commands.command;
import static matteo.blinkm.console.Commands.line;
import static matteo.blinkm.console.Commands.setScriptLengthAndReapeats;
import static matteo.blinkm.console.Commands.throwIfCommandIsNotEmbeddable;

import java.util.ArrayList;

public class Script {
	protected ArrayList<byte[]> script = new ArrayList<byte[]>();
	protected ArrayList<byte[]> commands = new ArrayList<byte[]>();
	
	public Script c(int addr, byte[] command) {
		this.commands.add(command(addr, command));
		return this;
	}
	
	public Script l(byte[] command) {
		throwIfCommandIsNotEmbeddable(command);
		this.script.add(command);
		return this;
	}
	
	public byte[][] commands() {
		byte[][] script = new byte[commands.size()][];
		for (int i = 0; i < commands.size(); i++) {
			script[i] = commands.get(i);			
		}
		return script;
	}
	
	public byte[][] script(int addr, int repeats) {
		byte[][] lines = new byte[script.size()+1][];
		for (int i = 0; i < script.size(); i++) {
			lines[i] = command(addr, line(i, script.get(i)));			
		}
		lines[lines.length-1] = command(addr, setScriptLengthAndReapeats(script.size(), repeats));
		return lines;
	}

}
