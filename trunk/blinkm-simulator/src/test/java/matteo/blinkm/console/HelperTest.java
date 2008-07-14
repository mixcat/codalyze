package matteo.blinkm.console;

import static org.junit.Assert.*;

import matteo.blinkm.Definition;
import matteo.blinkm.gui.ProcessingSimulatorClient;

import org.junit.Test;
import static matteo.blinkm.Definition.*;
public class HelperTest {

	@Test
	public void testInv() {
		char[] in = new char[] { 0, 1, 2, 3, 4 ,5 ,6 };
		char[] inv = Helper.inv(in);
		for (int i = 0; i < in.length; i++) {
			assertEquals(in[i], inv[in.length-i-1]);
		}
	}
	
	@Test
	public void testWrite() {
		Helper.client = new ProcessingSimulatorClient("foo", 00) {
			
			@Override
			public void write(char[] rt) {
				// TODO Auto-generated method stub
				//super.write(rt);
			}
		};
		
		Helper.write(goToRGB, 0);
		Helper.write(goToRGB, new char[] { 0,1,2 });
		Helper.write(goToRGB, new char[] { 0,0,0 }, 0);
	}
}
