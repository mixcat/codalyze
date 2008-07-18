package matteo.blinkm.console;

import static matteo.blinkm.Definition.*;
import static matteo.blinkm.Definition.setScriptLengthAndRepeats;
import static matteo.blinkm.console.Commands.COMMAND_ENVELOPE_LENGHT;
import static matteo.blinkm.console.Commands.COMMAND_LINE_LENGTH;
import static matteo.blinkm.console.Commands.fadeToRGB;
import static matteo.blinkm.console.Commands.setFadeSpeed;
import static matteo.blinkm.console.Commands.stopScript;
import static matteo.blinkm.console.VerifyUtils.getColor;
import static matteo.blinkm.console.VerifyUtils.verifyCommand;
import static matteo.blinkm.console.VerifyUtils.verifyScriptLineCommand;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static matteo.blinkm.console.VerifyUtils.*;
import java.awt.Color;

import org.junit.Before;
import org.junit.Test;

public class ScriptTest {
	
	@Test
	public void testScript() {
		Script script = new Script();
		Color color = Color.magenta;
		int fadeSpeed = 130;
		script.build(
			fadeToRGB(color),
			setFadeSpeed(fadeSpeed)
		);
		
		int repeats = 30;
		byte[] rt = script.get(addr, repeats);
		int scriptLengthAndReapeatsLength = COMMAND_ENVELOPE_LENGHT + setScriptLengthAndRepeats.getNumArgs() + 1;
		assertEquals(COMMAND_LINE_LENGTH*2 + scriptLengthAndReapeatsLength, rt.length );
		for (int i = 0; i < rt.length; i++) {
			assertFalse(i + "/" + rt.length +" should have been overwritten", '*' == rt[i]);
		}
		byte[] cmd;
		cmd = slice(rt, COMMAND_LINE_LENGTH*0, COMMAND_LINE_LENGTH);
		verifyScriptLineCommand(addr, cmd, fadeToRGB);
		assertEquals(color, getColor(rt[9], rt[10], rt[11] ));
		
		cmd = slice(rt, COMMAND_LINE_LENGTH*1, COMMAND_LINE_LENGTH);
		verifyScriptLineCommand(addr, cmd, setFadeSpeed);
		assertEquals((byte)fadeSpeed,cmd[9]);
		assertEquals(0, cmd[10]);
		assertEquals(0, cmd[11]);
		
		cmd = slice(rt, COMMAND_LINE_LENGTH*2, scriptLengthAndReapeatsLength);
		verifyCommand(addr, cmd, setScriptLengthAndRepeats);
		assertEquals((byte)0, cmd[5]); // scriptId: only script 0 is valid
		assertEquals((byte)2, cmd[6]);
		assertEquals((byte)repeats, cmd[7]);
	}
	
	@Test
	public void testScriptWithNonEmbeddableCommand() {
		Script script = new Script();
		try {
			script.build(
				fadeToRGB(Color.black),
				setFadeSpeed(0),
				stopScript()
			);
			fail("should have thrown because StopScript is not embeddable");
		} catch (RuntimeException e) {}
	}
	
	private byte addr;
	@Before
	public void setUp() {
		addr = 0x09;
	}
}
