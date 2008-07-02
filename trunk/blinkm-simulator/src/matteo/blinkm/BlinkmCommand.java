package matteo.blinkm;

import java.util.ArrayList;

public class BlinkmCommand {
	private final BlinkmCommandDef definition;
	private final ArrayList<Character> payload;

	BlinkmCommand(BlinkmCommandDef commandDef, char[] load) {
		this.definition =  commandDef;
		this.payload =  new ArrayList<Character>();
		for(char c: load) {
			payload.add(c);
		}
	}
	
	public BlinkmCommand(BlinkmCommandDef commandDef) {
		definition = commandDef;
		payload = new ArrayList<Character>();
	}

	public ArrayList<Character> getPayload() {
		return payload;
	}

	public BlinkmCommandDef getDefintion() {
		return definition;
	}
	
	public String toString() {
		String rt = this.definition.toString() + "[";
		for (char c : payload) {
			rt +=  c + ",";
		}
		return rt + "]";
		
	}

	public void addPayload(char c) {
		payload.add(c);
	}

}
	
