package matteo.blinkm.console;

import static org.junit.Assert.assertEquals;
import static matteo.blinkm.console.Commands.*;

import java.awt.Color;

import matteo.blinkm.ByteUtils;
import matteo.blinkm.Definition;

import org.junit.Before;
import org.junit.Test;
import static matteo.blinkm.console.VerifyUtils.*;
public class FadeToRGBTest {
	
	@Test
	public void testFadeToRGBColor() {
		Color t = Color.white;
		byte[] rt = fadeToRGB(t);
		assertEquals(Definition.fadeToRGB.getNumArgs()+1, rt.length);
		assertEquals(Definition.fadeToRGB.getCmd(), (char)rt[0]);
		assertEquals(t, ByteUtils.getColor(rt[1], rt[2], rt[3] ));
	}
	
	@Test
	public void testFadeToRGBColorCommand() {
		Color t = Color.white;
		byte[] cmd = fadeToRGB(t);
		byte[] rt = command(addr, cmd);
		verifyCommand(addr, rt, Definition.fadeToRGB);
		assertEquals(t, ByteUtils.getColor(rt[5], rt[6], rt[7] ));
	}
	
	@Test
	public void testFadeToRGBColorScriptLine() {
		Color t = Color.white;
		byte[] cmd = fadeToRGB(t);
		byte[] rt = line(0, cmd);
		verifyScriptLine(rt, Definition.fadeToRGB);
		assertEquals(t, ByteUtils.getColor(rt[5], rt[6], rt[7] ));
	}
	
	@Test
	public void testFadeToRGBColorScriptLineCommand() {
		Color arg = Color.white;
		byte[] cmd = fadeToRGB(arg);
		byte[] line = line(0, cmd);
		byte[] rt = command(addr, line);
		verifyScriptLineCommand(addr, rt, Definition.fadeToRGB);
		assertEquals(arg, ByteUtils.getColor(rt[9], rt[10], rt[11] ));
	}
	
	private byte addr;
	@Before
	public void setUp() {
		addr = 0x09;
	}
}
