package matteo.blinkm.graphics;

import matteo.blinkm.Blinkm;
import matteo.blinkm.Definition;
import matteo.blinkm.console.ProcessingSimulatorClient;
import matteo.blinkm.console.SimpleScript;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class ControllerTest {

	private Blinkm[] leds;
	private Controller controller;
	private char[][] matrix;
	private ProcessingSimulatorClient client;
	
	@Test
	public void testAllCommandsOneLed() {
		for(Definition def : Definition.values()) {
			char[] payload = new char[def.getNumArgs()];
			char[] cmd = SimpleScript.cmd(def, payload, new char[] {1});
			controller.dispatchReceivedCommands(cmd);
			client.write(cmd);
			
		}
		exercizeAllLeds();
	}
	
	@Test
	public void testAllCommandsOneLedWithSplit() {
		for(Definition def : Definition.values()) {
			char[] payload = new char[def.getNumArgs()];
			char[] cmd = SimpleScript.cmd(def, payload, new char[] { 1 });
			char[][] splitChar = splitChar(cmd, cmd.length/2);
			testMerged(cmd, controller.prepend(splitChar[0], splitChar[1]));
			controller.dispatchReceivedCommands(splitChar[0]);
			controller.dispatchReceivedCommands(splitChar[1]);
		}
		exercizeAllLeds();
	}
	
	@Test
	public void testStopScriptWithSplit() {
		char[] cmd = SimpleScript.cmd(Definition.stopScript, new char[]{}, new char[]{ 0 });
		char[][] splitChar = splitChar(cmd, cmd.length/2);
		testMerged(cmd, controller.prepend(splitChar[0], splitChar[1]));
		controller.dispatchReceivedCommands(splitChar[0]);
		controller.dispatchReceivedCommands(splitChar[1]);
		exercizeAllLeds();
	}

	private void exercizeAllLeds() {
		for (int i=0; i<10000; i++) {
			for (int j=0; j<leds.length; j++) {
				leds[j].getColor();
			}
		}
	}
	
	@Test
	public void testAllCommandsBroadcast() {
		for(Definition def : Definition.values()) {
			char[] payload = new char[def.getNumArgs()];
			char[] cmd = SimpleScript.cmd(def, payload, new char [] {0});
			controller.dispatchReceivedCommands(cmd);
		}
		
		exercizeAllLeds();
	}
	
	@Test
	public void testScriptWithOneCommandToOneLed() {
		char[] cmd = new SimpleScript()
			.line(200, Definition.fadeToRGB, new char[] { 0, 0, 0 }).play((char)0, (char)1);
		controller.dispatchReceivedCommands(cmd);
		controller.dispatchReceivedCommands(SimpleScript.START);
		exercizeAllLeds();
	}
	
	@Test
	public void testScriptWithOneCommandToMultipleLeds() {
		char[] cmd = new SimpleScript()
			.line(200, Definition.fadeToRGB, new char[] { 0, 255, 0 })
			.line(200, Definition.fadeToRGB, new char[] { 0, 0, 0 }).play((char)0, Matrix.flatten(matrix, 10, 10));
		controller.dispatchReceivedCommands(cmd);
		controller.dispatchReceivedCommands(SimpleScript.START);
		exercizeAllLeds();
		client.write(cmd);
		client.write(SimpleScript.START);
	}
	
	//@Test
	public void testLongScriptWithOneCommandToMultipleLedsAndRandomSplit() {
		char[] cmd = new SimpleScript()
			.line(200, Definition.fadeToRGB, new char[] { 0, 0, 0 })
			.line(0, Definition.fadeToRGB, new char[] { 255, 255, 255 })
			.line(255, Definition.fadeToRGB, new char[] { 255, 255, 255 })
			.line(255, Definition.fadeToHSB, new char[] { 255, 255, 255 })
		.play((char)0, Matrix.flatten(matrix, 10, 10));
		
		for (int i=0; i<cmd.length; i++) {
			char[][] splitChar = splitChar(cmd, i);
			controller.dispatchReceivedCommands(splitChar[0]);
			char[][] splitChar2 = splitChar(splitChar[1], splitChar[1].length);
			controller.dispatchReceivedCommands(splitChar2[0]);
			controller.dispatchReceivedCommands(splitChar2[1]);
			char[] play = SimpleScript.cmd(Definition.playScript, new char[] {0, 0, 0}, Matrix.flatten(matrix, 10, 10));
			controller.dispatchReceivedCommands(play);
			//client.write(cmd);
			exercizeAllLeds();
		}
		
	}
	
	public void testMerged(char[] orig, char[] merged) {
		if (orig.length != merged.length) {
			fail();
		}
		assertEquals(orig.length, merged.length);
		for (int i=0; i<orig.length;i++) {
			if (orig[i] != merged[i]) {
				fail();
			}
			assertEquals(orig[i], merged[i]);
		}
	}
	
	@Test
	public void testPrepend() {
		char[] in = new char[] {'A','B','C','D','E','F','G'};
		char[][] splitChar = splitChar(in, 3);
		char[] prepend = controller.prepend(splitChar[0], splitChar[1]);
		for (int i=0; i<in.length;i++) {
			assertEquals(in[0], prepend[0]);
		}
	}
	
	@Test
	public void testSplitChar() {
		char[] in = new char[] {'A','B','C','D','E','F','G'};
		char[][] splitChar = splitChar(in, 3);
		char[] a = splitChar[0];
		char[] b = splitChar[1];
		assertEquals(3, a.length);
		assertEquals(4, b.length);
		assertEquals('A', a[0]);
		assertEquals('B', a[1]);
		assertEquals('C', a[2]);
		assertEquals('D', b[0]);
		assertEquals('E', b[1]);
		assertEquals('F', b[2]);
		assertEquals('G', b[3]);
	}
	
	private char[][] splitChar(char[] in, int pos) {
		char[] a = new char[pos];
		char[] b = new char[in.length-pos];
		for (int i=0;i<pos;i++) {
			a[i] = in[i];
		}
		
		for (int i=pos; i<in.length; i++) {
			b[i-pos] = in[i];
		}
		if (in.length!=+a.length+b.length) {
			fail();
		}
		assertEquals(in.length, a.length+b.length);
		return new char[][] { a, b };
	}
	
	@After
	public void tearDown() {
		//client.disconnect();
	}
	
	@Before
	public void setUp() {
		leds = new Blinkm[100];
		for (int i=0; i<leds.length; i++) {
			leds[i] = new Blinkm((byte)i);
		}
		controller = new Controller(leds);
		int cols = 10;
		int rows = 10;
		matrix = Matrix.build(rows,cols);
		client = new ProcessingSimulatorClient("localhost", 5204);
		client.connect();
	}
}
