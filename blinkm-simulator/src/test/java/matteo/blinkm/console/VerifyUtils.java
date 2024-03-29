package matteo.blinkm.console;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import matteo.blinkm.Definition;
public class VerifyUtils {
	static void verifyCommand(byte addr, byte[] rt, Definition def) {
		assertEquals(def.getNumArgs()+1, rt.length);
		assertEquals((byte)addr, rt[1]);
		assertEquals((byte)def.getCmd(), rt[4]);
	}
	
	static void verifyScriptLine(byte[] rt, Definition def) {
		assertEquals(Definition.writeScriptLine.getNumArgs()+1, rt.length);
		assertEquals((byte)Definition.writeScriptLine.getCmd(), rt[0]);
		assertEquals((byte)0, rt[1]);   //script id
		assertEquals((byte)0, rt[2]);   // line no
		assertEquals((byte)255, rt[3]); // duration
		assertEquals((byte)def.getCmd(), rt[4]);
	}
	
	static void verifyScriptLineCommand(byte addr, byte[] rt, Definition def) {
		assertEquals(Definition.writeScriptLine.getNumArgs()+1, rt.length);
		assertEquals((byte)addr, rt[1]);
		assertEquals(Definition.writeScriptLine.getCmd(), (char)rt[4]);
		assertEquals((byte)0, rt[5]);   //script id
		assertEquals((byte)0, rt[6]);   // line no
		assertEquals((byte)255, rt[7]); // duration
		assertEquals((byte)def.getCmd(), rt[8]);
	}
	
	static void verifyLineThrowsNotEmbeddable(byte[] cmd, Definition def) {
		try {
			Commands.line(0, cmd);
			fail("Expected exception: Command not embeddable in a script line. ");
		} catch (RuntimeException e) {
			assertEquals(e.getMessage(), "Command [" + def.toString()+ "] not embeddable in a script line");
		}
	}
	
}
