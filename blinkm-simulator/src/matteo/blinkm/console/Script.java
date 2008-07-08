package matteo.blinkm.console;

import java.util.ArrayList;


public class Script {
	private ArrayList<ScriptBuilder> scripts = new ArrayList<ScriptBuilder>();
	public ScriptBuilder script(int repeats, int addr) {
		ScriptBuilder builder = new ScriptBuilder(this, addr, repeats);
		scripts.add(builder);
		return builder;
	}
	public ArrayList<Character> chars() {
		ArrayList<Character> list = new ArrayList<Character>();
		for (ScriptBuilder s : scripts) {
			list.addAll(s.bytes());
		}
		return list;
	}
}
