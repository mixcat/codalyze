package matteo.blinkm.console;

import java.util.Iterator;

public class MatrixIterator implements Iterator<Character> {

	private final char[][] matrix;
	private int i;
	private int j;
	private int rows;
	private int cols;

	public MatrixIterator(char[][] matrix) {
		this.matrix = matrix;
		i = 0;
		j = 0;
		rows = matrix.length;
		cols = matrix[0].length;
	}

	@Override
	public boolean hasNext() {
		return (i<rows || j<cols);
	}

	@Override
	public Character next() {
		char c = matrix[i][j];
		i++;
		j++;// TODO Auto-generated method stub
		System.out.println("returning addr " + (int)c);
		return c;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}
}
