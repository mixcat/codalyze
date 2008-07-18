package matteo.blinkm.console;

import static matteo.blinkm.console.VerifyUtils.verifyCommand;
import static matteo.blinkm.console.VerifyUtils.verifyLineThrowsNotEmbeddable;
import static org.junit.Assert.assertEquals;
import matteo.blinkm.Definition;
import static matteo.blinkm.console.Commands.*;

import org.junit.Before;
import org.junit.Test;

public class SetScriptLengthAndRepeatsTest {

	@Test
	public void testSetScriptLengthAndRepeats() {
		int length = 5;
		int repeats = 200;
		byte[] rt = setScriptLengthAndReapeats(length, repeats);
		assertEquals(Definition.setScriptLengthAndRepeats.getNumArgs()+1, rt.length);
		assertEquals(Definition.setScriptLengthAndRepeats.getCmd(), (char)rt[0]);
		assertEquals((byte)0, rt[1]); // scriptId: only script 0 is valid
		assertEquals((byte)length, rt[2]);
		assertEquals((byte)repeats, rt[3]);
	}
	
	@Test
	public void testSetScriptLengthAndRepeatsCommand() {
		int length = 5;
		int repeats = 200;
		byte[] rt = command(addr, setScriptLengthAndReapeats(length, repeats));
		verifyCommand(addr, rt, Definition.setScriptLengthAndRepeats);
		assertEquals((byte)0, rt[5]); // scriptId: only script 0 is valid
		assertEquals((byte)length, rt[6]);
		assertEquals((byte)repeats, rt[7]);
	}
	
	@Test
	public void testSetScriptLengthAndRepeatsLine() {
		byte[] cmd = setScriptLengthAndReapeats(0, 0);
		verifyLineThrowsNotEmbeddable(cmd, Definition.setScriptLengthAndRepeats);
	}
	
	private byte addr;
	@Before
	public void setUp() {
		addr = 0x09;
	}

}
