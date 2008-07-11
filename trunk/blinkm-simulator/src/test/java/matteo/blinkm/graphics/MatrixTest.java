package matteo.blinkm.graphics;

import static org.junit.Assert.*;

import matteo.blinkm.graphics.Matrix.Direction;

import org.junit.Before;
import org.junit.Test;

public class MatrixTest {

	private char[][] matrix;

	@Test
	public void testFlatten() {
		char[] flatten = Matrix.flatten(matrix, 10, 10);
		char i = 1;
		for (char c : flatten) {
			System.out.println((int)c);
			assertEquals(i, c);
			i++;
		}
	}
	
	@Test
	public void testLineNorth() {
		char[] line = Matrix.line(matrix, 9, 0, Direction.north, 10);
		assertEquals(10, line.length);
		char expected = 91;
		for (char addr : line) {
			System.out.println((int)addr);
			assertEquals(expected, addr);
			expected -= 10;
		}
	}
	
	@Test
	public void testLineEast() {
		char[] line = Matrix.line(matrix, 0, 0, Direction.east, 10);
		assertEquals(10, line.length);
		char expected = 1;
		for (char addr : line) {
			assertEquals(expected, addr);
			expected += 1;
		}
	}
	
	@Test
	public void testLineSouth() {
		char[] line = Matrix.line(matrix, 0, 9, Direction.south, 10);
		assertEquals(10, line.length);
		char expected = 10;
		for (char addr : line) {
			assertEquals(expected, addr);
			expected += 10;
		}
	}
	
	@Test
	public void testLineWest() {
		char[] line = Matrix.line(matrix, 9, 9, Direction.west, 10);
		assertEquals(10, line.length);
		char expected = 100;
		for (char addr : line) {
			assertEquals(expected, addr);
			expected -= 1;
		}
	}
	
	@Test
	public void testSquare() {
		char[] square = Matrix.square(matrix, 0, 0, 10);
		assertEquals(10*2+8*2,square.length);
	}
	
	@Before
	public void setUp() {
		matrix = Matrix.build(10, 10);
	}
}
