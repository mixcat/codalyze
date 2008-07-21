package matteo.blinkm;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.awt.Color;

import matteo.blinkm.console.Commands;
import matteo.blinkm.console.Helper;
import matteo.blinkm.console.Script;
import matteo.blinkm.console.SimpleScript;
import matteo.blinkm.graphics.Matrix;
import matteo.blinkm.graphics.Matrix.Direction;

import static org.easymock.classextension.EasyMock.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static matteo.blinkm.console.Commands.*;
public class ControllerTest {

	private Blinkm[] leds;
	private Controller controller;
	private Blinkm[] mockLeds;
	private Controller controllerM;

	@Test
	public void testReceiveFadeToRGB() {
		byte[] cmd = fadeToRGB(Color.white);
		byte[] command = command((byte)9, cmd);
		controller.receive(command);
		assertNull(controller.getReminder());
		assertEquals(Color.white, leds[9].getFadeToColor());
	}
	
	@Test
	public void testReceiveFadeToRGBMultipleTimes() {
		Color[] colors = new Color[] { Color.white, Color.green, Color.yellow };
		for (Color color : colors) {
			controller.receive(command((byte)7, fadeToRGB(color) ));
			assertNull(controller.getReminder());
			assertEquals(color, leds[7].getFadeToColor());
		}
	}
	
	@Test
	public void testReceiveMultipleFadeToRGBConcatenated() {
		byte[] command = command((byte)7, fadeToRGB(Color.yellow) );
		byte[] command2 = command((byte)7, fadeToRGB(Color.blue) );
		byte[] merged = ByteUtils.merge(command, command2);
		controller.receive(merged);
		assertEquals(Color.blue, leds[7].getFadeToColor());
	}
	
	@Test
	public void testReceivePlayScript() {
		byte[] command = command((byte)9, playScript(0, 0, 0));
		controller.receive(command);
		assertTrue(leds[9].isScriptRunning());
	}
	
	@Test
	public void testReceiveSetScriptLengthAndRepeats() {
		byte[] command = command((byte)9, setScriptLengthAndReapeats(27, 32));
		controller.receive(command);
		assertEquals(27, leds[9].getCustomScriptLength());
		assertEquals(32, leds[9].getCustomScriptRepeats());
	}
	
	@Test
	public void testDispatchFadeToRGB() {
		byte[] command =  fadeToRGB(Color.white);
		mockLeds[9].setCmd(command);
		replay(mockLeds[9]);
		controllerM.dispatch((byte)9, command);
		verify(mockLeds[9]);
	}
	
	@Test
	public void testReceiveSplittedFadeToRGB() {
		byte[] cmd = fadeToRGB(Color.white);
		byte[] command = command((byte)9, cmd);
		byte[][] split = ByteUtils.split(command, command.length/2);
		controller.receive(split[0]);
		assertEquals(4, controller.getReminder().length);
		controller.receive(split[1]);
		assertNull(controller.getReminder());
		assertEquals(Color.white, leds[9].getFadeToColor());
	}
	
	@Test
	public void testReceiveScript() {
		Script script = new Script();
		script.build(new byte[][] {
			Commands.fadeToRGB(Color.white),
			Commands.fadeToRGB(Color.blue)}
		);
		byte[] bs = script.get((byte)9, (byte)0);
		controller.receive(bs);
		assertEquals(2, leds[9].getCustomScriptLength());
		assertEquals(0, leds[9].getCustomScriptRepeats());
	}
	
	@Before
	public void setUp() {
		leds = new Blinkm[100];
		mockLeds = new Blinkm[100];
		for (int i=0; i<100; i++) {
			mockLeds[i] = createMock(Blinkm.class);
			leds[i] = new Blinkm((byte) (i+1));
		}
		controller = new Controller(leds);
		controllerM = new Controller(mockLeds);
	}
	
}
