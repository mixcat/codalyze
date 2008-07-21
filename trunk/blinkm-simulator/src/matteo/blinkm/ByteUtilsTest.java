package matteo.blinkm;
import static junit.framework.Assert.*;

import org.junit.Test;

public class ByteUtilsTest {

	@Test
	public void testSplit() {
		byte[] src = new byte[] { 1,2,3,4,5,6,7,8 };
		byte[][] split = ByteUtils.split(src, 4);
		
		byte[] a = split[0];
		byte[] b = split[1];
		
		assertEquals(1, a[0]);
		assertEquals(2, a[1]);
		assertEquals(3, a[2]);
		assertEquals(4, a[3]);
		assertEquals(5, b[0]);
		assertEquals(6, b[1]);
		assertEquals(7, b[2]);
		assertEquals(8, b[3]);
	}
	
	public void testUnpad() {
		fail();
	}
}
