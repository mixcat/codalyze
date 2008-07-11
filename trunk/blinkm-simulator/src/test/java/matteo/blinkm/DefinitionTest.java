package matteo.blinkm;

import static org.junit.Assert.*;

import org.junit.Test;

public class DefinitionTest {

	@Test
	public void testValidateAllCommandsCorrect() {
		Definition[] values = Definition.values();
		for (Definition definition : values) {
			int numArgs = definition.getNumArgs();
			char[] payload = buildPayload(numArgs);
			definition.validate(payload);
		}
	}
	
	@Test 
	public void testValidateAllCommandsLong() {
		Definition[] values = Definition.values();
		for (Definition definition : values) {
			int numArgs = definition.getNumArgs();
			char[] payload = buildPayload(numArgs+1);
			try {
				definition.validate(payload);
				fail();
			} catch (Exception e) {
			}
		}
	}
	
	@Test 
	public void testValidateAllCommandsShort() {
		Definition[] values = Definition.values();
		for (Definition definition : values) {
			int numArgs = definition.getNumArgs();
			if (numArgs==0)
				continue;
			char[] payload = buildPayload(numArgs-1);
			try {
				definition.validate(payload);
				fail();
			} catch (Exception e) {
			}
		}
	}

	private char[] buildPayload(int numArgs) {
		char[] payload = new char[numArgs];
		for (int i = 0; i < payload.length; i++) {
			payload[i] = '*';
		}
		return payload;
	}
}
