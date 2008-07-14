package matteo.blinkm;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;

import com.sun.org.apache.bcel.internal.generic.FADD;

public class BlinkmTest {

	private Blinkm b;

	@Test
	public void testFadeToRGBBlackToWhiteFadeStepOneTimeAdjustZero() {
		b.fadeSpeed = 1;
		b.timeAdjiust = 128;
		b.fadeToColor = Color.white;	
		
		for (int i=1; i<255; i++) {
			Color color = b.getColor();
			assertEquals(new Color(i, i, i), color);
		}
	}
	
	@Test
	public void testFadeToRGBBlackToWhiteFadeStepTwoTimeAdjustZero() {
		b.fadeSpeed = 2;
		b.timeAdjiust = 128;
		b.fadeToColor = Color.white;	
		
		int i=0;
		Color color = Color.black;
		Color oldColor;
		for (i=1; i<255; i++) {
			color = b.getColor();
			System.out.println((int)b.fadeSpeed);
			if (color.equals(Color.white))
				break;
		}
		assertEquals(Color.white, color);
		assertEquals(128, i);
	}
	
	
	@Test
	public void testGetFadeStep() {
		Color color = b.getFadeStep(Color.black, Color.white, (char) 1);
		System.out.println("--" + color.getRed() + " " + color.getGreen() + " " + color.getBlue());
		assertEquals(new Color(1,1,1), color);
	}
	
	@Test
	public void testGetFadeComponent() {
		double fadeComponent = b.getFadeComponent(0, 255, (char) 1);
		assertEquals(1, fadeComponent);
	}
	
	@Before
	public void setUp() {
		b = new Blinkm((byte) 1);
	}
	
	
}
