package matteo.blinkm.console;

import static matteo.blinkm.console.VerifyUtils.getColor;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static matteo.blinkm.console.Commands.*;
import java.awt.Color;
import static matteo.blinkm.Definition.*;
import junit.framework.Assert;
import static matteo.blinkm.console.VerifyUtils.*;
import matteo.blinkm.Definition;

import org.junit.Before;
import org.junit.Test;
public class CommandsTest {


	@Test
	public void testCastIntToByte() {
		for (int i=128; i<256; i++) {
			assertEquals(i, (int)( (byte)i) & 0xff );
		}
	}
	
	//@Test
	public void testGetColor() {
		for (int r=0; r<256; r++)
			for (int g=0; g<256; g++)
				for (int b=0; b<256; b++)
					assertEquals(new Color(r,g,b), getColor((byte)r,(byte)g,(byte)b));
	}

	private byte addr;
	@Before
	public void setUp() {
		addr = 0x09;
	}
	
}
