package matteo.blinkm.console;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.print.attribute.Size2DSyntax;

public class DiagonalMatrixIterator implements Iterator<Character>, Iterable<Character> {

	private final char[][] matrix;
	private int i;
	private int j;
	private int rows;
	private int cols;
	private final int dir;
	private int idir;
	private int jdir;

	public DiagonalMatrixIterator(final char[][] matrix, int dir) {
		this.matrix = matrix;
		this.dir = dir;
		rows = matrix.length;
		cols = matrix[0].length;
		
		switch (dir) {
		case 1:
			i = 0;
			j = 0;
			idir = 1;
			jdir = 1; 
		break;
		case -1:
			i = rows-1;
			j = 0;
			idir = -1;
			jdir = 1;
		break;
		}
	}

	@Override
	public boolean hasNext() {
		if (dir == -1)
			return (i>=0 || j<cols);
		return(i<rows || j<cols);
	}

	@Override
	public Character next() {
		System.out.println(i + " " + j);
		char c = matrix[i][j];
		i+=idir;
		j+=jdir;
		return c;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		throw new RuntimeException("remove() not implemented");
	}

	@Override
	public Iterator iterator() {
		return this;
	}
}
