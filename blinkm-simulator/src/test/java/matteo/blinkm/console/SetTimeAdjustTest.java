package matteo.blinkm.console;

import static matteo.blinkm.console.Commands.command;
import static matteo.blinkm.console.Commands.line;
import static matteo.blinkm.console.Commands.setTimeAdjust;
import static matteo.blinkm.console.VerifyUtils.verifyCommand;
import static matteo.blinkm.console.VerifyUtils.verifyScriptLine;
import static org.junit.Assert.assertEquals;
import matteo.blinkm.Definition;

import org.junit.Before;
import org.junit.Test;

public class SetTimeAdjustTest {
	@Test
	public void testSetTimeAdjust() {
		int timeAdjust = 20;
		byte rt[] = setTimeAdjust(timeAdjust);
		assertEquals(Definition.setTimeAdjust.getNumArgs()+1, rt.length);
		assertEquals(Definition.setTimeAdjust.getCmd(), (char)rt[0]);
		assertEquals(timeAdjust, rt[1]);
	}
	
	@Test
	public void testSetTimeAdjustCommand() {
		int timeAdjust = 20;
		byte[] cmd = setTimeAdjust(timeAdjust);
		byte[] rt = command(addr, cmd);
		verifyCommand(addr, rt, Definition.setTimeAdjust);
		assertEquals(timeAdjust, rt[5]);
	}

	@Test
	public void testSetTimeAdjustScriptLine() {
		int timeAdjust = 255;
		byte[] cmd = setTimeAdjust(timeAdjust);
		byte[] rt = line(0, cmd);
		verifyScriptLine(rt, Definition.setTimeAdjust);
		assertEquals((byte)timeAdjust, rt[5]);
	}
	
	private byte addr;
	@Before
	public void setUp() {
		addr = 0x09;
	}
}
