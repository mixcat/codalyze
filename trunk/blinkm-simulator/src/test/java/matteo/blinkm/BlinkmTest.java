package matteo.blinkm;

import java.awt.Color;

import matteo.blinkm.console.Commands;

import org.junit.Test;
import static matteo.blinkm.console.Commands.*;
import static org.junit.Assert.*;

public class BlinkmTest {

	
	public void testGetCurrentScriptLine() {
		byte addr = 0x09;
		Blinkm blinkm = new Blinkm(addr);
		
		blinkm.setCmd(Commands.line(0,fadeToRGB(Color.red)));
		blinkm.setCmd(Commands.line(1,fadeToRGB(Color.orange)));
		blinkm.setCmd(setScriptLengthAndReapeats(2, 0));
		blinkm.setCmd(Commands.playScript(0, 0, 0));
		blinkm.currentScriptTick = 0;
		for (int i=0; i<1000; i++) {
			int l = 0;//blinkm.getCurrentScriptLine();
			System.out.println(i+":"+l);
			if (i<256) assertEquals(0,l );
			if (i>256 && i<511) assertEquals(1, l);
			if (i>510 && i<766) assertEquals(0, l);
			if (i>766) assertEquals(1, l);
		}
	}
	
	@Test
	public void testSimpleScript() {
		byte addr = 0x09;
		Blinkm blinkm = new Blinkm(addr);
		
		blinkm.setCmd(Commands.line(0,fadeToRGB(Color.red)));
		blinkm.setCmd(Commands.line(1,fadeToRGB(Color.orange)));
		blinkm.setCmd(Commands.line(2,fadeToRGB(Color.green)));
		blinkm.setCmd(Commands.line(3,fadeToRGB(Color.blue)));
		blinkm.setCmd(setScriptLengthAndReapeats(4, 0));
		blinkm.setCmd(Commands.playScript(0, 0, 0));
		
		assertEquals(4, blinkm.getCustomScriptLength());
		byte[][] script = blinkm.getScript(0);
		assertEquals((byte)255, script[0][0]);
		assertEquals((byte)Definition.fadeToRGB.getCmd(), script[0][1]);
		assertEquals((byte)255, script[1][0]);
		assertEquals((byte)Definition.fadeToRGB.getCmd(), script[1][1]);
		assertEquals((byte)255, script[2][0]);
		assertEquals((byte)Definition.fadeToRGB.getCmd(), script[2][1]);
		assertEquals((byte)255, script[3][0]);
		assertEquals((byte)Definition.fadeToRGB.getCmd(), script[2][1]);
		
	}
	
	@Test
	public void testGetFadeComponent() {
		double to = 255;
		for (double from=0; from<255; from++) {
			verifyGetFadeComponent(from+1, from, to, (byte)1);
		}
		for (double from=0; from<255; from++) {
			verifyGetFadeComponent(to-1, to, from, (byte)1);
		}
		for (double from=0; from<255; from++) {
			verifyGetFadeComponent(from+2, from, to, (byte)2);
		}
		for (double from=0; from<255; from++) {
			verifyGetFadeComponent(from+5, from, to, (byte)5);
		}
		verifyGetFadeComponent(255L, 255L, 255L, (byte)5);
	}
	
	private void verifyGetFadeComponent(double expected, double from, double to, byte fadeSpeed) {
		try {
			double rt = Blinkm.getFadeComponent(from, to, fadeSpeed);
			assertEquals(expected, rt);
			assertTrue(rt+" ", rt < 256);
		} catch (Exception e) {
			throw new RuntimeException("from: " + from + " to: " + to + " speed: " + (fadeSpeed&0xFF) + " "  + e.getMessage());
		}
		
	}
}
