package matteo.blinkm.console;

import static matteo.blinkm.console.VerifyUtils.verifyCommand;
import static matteo.blinkm.console.VerifyUtils.verifyScriptLine;
import static matteo.blinkm.console.VerifyUtils.verifyScriptLineCommand;
import static org.junit.Assert.assertEquals;
import matteo.blinkm.Definition;

import org.junit.Before;
import org.junit.Test;
import static matteo.blinkm.console.Commands.*;
public class SetFadeSpeedTest {
	
	@Test
	public void testSetFadeSpeed() {
		int fadeSpeed = 20;
		byte rt[] = setFadeSpeed(fadeSpeed);
		assertEquals(Definition.setFadeSpeed.getNumArgs()+1, rt.length);
		assertEquals(Definition.setFadeSpeed.getCmd(), (char)rt[0]);
		assertEquals(fadeSpeed, rt[1]);
	}
	
	@Test
	public void testSetFadeSpeedCommand() {
		int fadeSpeed = 20;
		byte[] cmd = setFadeSpeed(fadeSpeed);
		byte[] rt = command(addr, cmd);
		verifyCommand(addr, rt, Definition.setFadeSpeed);
		assertEquals(fadeSpeed, rt[5]);
	}

	@Test
	public void testSetFadeSpeedScriptLine() {
		int fadeSpeed = 255;
		byte[] cmd = setFadeSpeed(fadeSpeed);
		byte[] rt = line(0, cmd);
		verifyScriptLine(rt, Definition.setFadeSpeed);
		assertEquals((byte)fadeSpeed, rt[5]);
	}

	@Test
	public void testSetFadeSpeedScriptLineCommand() {
		int fadeSpeed = 0;
		byte[] cmd = setFadeSpeed(fadeSpeed);
		byte[] line = line(0, cmd);
		byte[] rt = command(addr, line);
		verifyScriptLineCommand(addr, rt, Definition.setFadeSpeed);
		assertEquals((byte)fadeSpeed,rt[9]);
		assertEquals(0, rt[10]);
		assertEquals(0, rt[11]);
	}
	
	private byte addr;
	@Before
	public void setUp() {
		addr = 0x09;
	}
}
