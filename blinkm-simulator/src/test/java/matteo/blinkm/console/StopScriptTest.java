package matteo.blinkm.console;

import static matteo.blinkm.console.Commands.command;
import static matteo.blinkm.console.Commands.stopScript;
import static matteo.blinkm.console.VerifyUtils.verifyCommand;
import static matteo.blinkm.console.VerifyUtils.verifyLineThrowsNotEmbeddable;
import static org.junit.Assert.assertEquals;
import matteo.blinkm.Definition;

import org.junit.Before;
import org.junit.Test;

public class StopScriptTest {
	
	@Test
	public void testStopScript() {
		byte[] rt = stopScript();
		assertEquals(Definition.stopScript.getNumArgs()+1, rt.length);
		assertEquals(Definition.stopScript.getCmd(), (char)rt[0]);
	}
	
	@Test
	public void testStopScriptCommand() {
		byte[] cmd = stopScript();
		byte[] rt = command(addr, cmd);
		verifyCommand(addr, rt, Definition.stopScript);
		assertEquals((byte)Definition.stopScript.getCmd(), rt[4]);
	}
	
	@Test
	public void testStopScriptScriptLine() {
		byte[] cmd = stopScript();
		verifyLineThrowsNotEmbeddable(cmd, Definition.stopScript);
	}
	
	
	private byte addr;
	@Before
	public void setUp() {
		addr = 0x09;
	}
}
