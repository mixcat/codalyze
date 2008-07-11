package matteo.blinkm.console;

import static org.junit.Assert.*;
import matteo.blinkm.graphics.Matrix;

import org.junit.Before;
import org.junit.Test;

public class DiagonalMatrixIteratorTest {

	private char[][] matrix;
	private DiagonalMatrixIterator iterator;

	@Test
	public void testMainDiagonal() {
		iterator = new DiagonalMatrixIterator(matrix, 1);
		int i = 0;
		char n=1;
		for (Character d : iterator) {
			i++;
			assertEquals(n, d);
			n+=11;
		}
		assertEquals(10, i);
	}
	
	@Test
	public void testInverseMainDiagonal() {
		iterator = new DiagonalMatrixIterator(matrix, -1);
		int i = 0;
		char n=100;
		for (Character d : iterator) {
			i++;
			System.out.println((int)d);
			assertEquals(n, d);
			n-=11;
		}
		assertEquals(10, i);
	}
	
	@Test
	public void testMinorDiagonal() {
		iterator = new DiagonalMatrixIterator(matrix, -1);
		int i = 0;
		char n=91;
		for (Character d : iterator) {
			i++;
			System.out.println((int)d);
			assertEquals(n, d);
			n-=9;
		}
		assertEquals(10, i);
	}
	
	@Before
	public void setUp() {
		matrix = Matrix.build(10, 10);
		
	}
}
