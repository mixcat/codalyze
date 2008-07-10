package matteo.blinkm.console;

import java.util.ArrayList;

import matteo.blinkm.Definition;

public class Command {
	private final Definition definition;
	private final ArrayList<Character> payload = new ArrayList<Character>();

	public Command(Definition commandDef, char[] load) {
		this.definition =  commandDef;
		for(char c: load) {
			payload.add(c);
		}
	}
	
	public Command(Definition commandDef) {
		definition = commandDef;
	}

	public Command(char c) {
		definition = Definition.getByChar(c);
		if (definition == null) {
			throw new RuntimeException("command not found. idx: " + c);
		}
	}

	public ArrayList<Character> getPayload() {
		return payload;
	}

	public Definition getDefintion() {
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

	public boolean isComplete() {
		return (payload.size() == definition.getNumArgs());
	}

	public void validate () {
		char[] rt = new char[payload.size()];
		for (int i = 0; i <payload.size(); i++) {
			rt[i] = payload.get(i);
		}
		this.definition.validate(rt);
	}

}
	
