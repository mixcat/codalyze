package matteo.blinkm.console;

import static matteo.blinkm.console.VerifyUtils.verifyCommand;
import static matteo.blinkm.console.VerifyUtils.verifyScriptLine;
import static matteo.blinkm.console.VerifyUtils.verifyScriptLineCommand;
import static org.junit.Assert.assertEquals;
import matteo.blinkm.Definition;
import static matteo.blinkm.console.Commands.*;

import org.junit.Before;
import org.junit.Test;

public class PlayScriptTest {

	@Test
	public void testPlayScript() {
		int scriptId = 0;
		int repeat = 120;
		int startLine = 200;
		byte[] rt = playScript(scriptId, repeat, startLine);
		assertEquals(Definition.playScript.getNumArgs()+1, rt.length);
		assertEquals(Definition.playScript.getCmd(), (char)rt[0]);
		assertEquals((byte)scriptId, rt[1]);
		assertEquals((byte)repeat, rt[2]);
		assertEquals((byte)startLine, rt[3]);
	}
	
	@Test
	public void testPlayScriptScriptLine() {
		int scriptId = 0;
		int repeat = 120;
		int startLine = 200;
		byte[] rt = line(playScript(scriptId, repeat, startLine));
		verifyScriptLine(rt, Definition.playScript);
		assertEquals((byte)scriptId, rt[5]);
		assertEquals((byte)repeat, rt[6]);
		assertEquals((byte)startLine, rt[7]);
	}

	@Test
	public void testPlayScriptCommand() {
		int scriptId = 0;
		int repeat = 120;
		int startLine = 200;
		byte[] rt = command(addr, playScript(scriptId, repeat, startLine));
		verifyCommand(addr, rt, Definition.playScript);
		assertEquals((byte)scriptId, rt[5]);
		assertEquals((byte)repeat, rt[6]);
		assertEquals((byte)startLine, rt[7]);
	}
	
	public void testPlayScriptScriptLineCommand() {
		int scriptId = 0;
		int repeat = 120;
		int startLine = 200;
		byte[] rt = command(addr, line(playScript(scriptId, repeat, startLine)));
		verifyScriptLineCommand(addr, rt, Definition.playScript);
		assertEquals((byte)scriptId, rt[9]);
		assertEquals((byte)repeat, rt[10]);
		assertEquals((byte)startLine, rt[11]);
	}
	
	private byte addr;
	@Before
	public void setUp() {
		addr = 0x09;
	}
}
