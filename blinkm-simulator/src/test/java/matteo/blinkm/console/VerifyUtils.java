package matteo.blinkm.console;

import static org.junit.Assert.assertEquals;
import static matteo.blinkm.console.Commands.*;
import static org.junit.Assert.fail;

import java.awt.Color;

import matteo.blinkm.Definition;
public class VerifyUtils {
	static void verifyCommand(byte addr, byte[] rt, Definition def) {
		assertEquals(def.getNumArgs()+1+COMMAND_ENVELOPE_LENGHT, rt.length);
		assertEquals((byte)0x01, rt[0]);
		assertEquals((byte)addr, rt[1]);
		assertEquals(def.getNumArgs()+1, rt[2]);
		assertEquals((byte)0x00, rt[3]);
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
		assertEquals(Definition.writeScriptLine.getNumArgs()+1+COMMAND_ENVELOPE_LENGHT, rt.length);
		assertEquals((byte)0x01, rt[0]);
		assertEquals((byte)addr, rt[1]);
		assertEquals(Definition.writeScriptLine.getNumArgs()+1, rt[2]);
		assertEquals((byte)0x00, rt[3]);
		assertEquals(Definition.writeScriptLine.getCmd(), (char)rt[4]);
		assertEquals((byte)0, rt[5]);   //script id
		assertEquals((byte)0, rt[6]);   // line no
		assertEquals((byte)255, rt[7]); // duration
		assertEquals((byte)def.getCmd(), rt[8]);
	}
	
	static void verifyLineThrowsNotEmbeddable(byte[] cmd, Definition def) {
		try {
			Commands.line(cmd);
			fail("Expected exception: Command not embeddable in a script line. ");
		} catch (RuntimeException e) {
			assertEquals(e.getMessage(), "Command [" + def.toString()+ "] not embeddable in a script line");
		}
	}
	
	static Color getColor(byte r, byte g, byte b) {
		return new Color( (int)r&0xff, (int)g&0xff, (int)b&0xff);
	}
	
	static byte[] slice(byte[] src, int start, int len) {
		byte[] rt = new byte[len];
		for (int i=0; i<len; i++) {
			rt[i] = src[start+i];
		}
		return rt;
	}
}
