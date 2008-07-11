package matteo.blinkm.console;

import static org.junit.Assert.*;

import org.junit.Test;

public class HelperTest {

	@Test
	public void testInv() {
		char[] in = new char[] { 0, 1, 2, 3, 4 ,5 ,6 };
		char[] inv = Helper.inv(in);
		for (int i = 0; i < in.length; i++) {
			assertEquals(in[i], inv[in.length-i-1]);
		}
	}
}
