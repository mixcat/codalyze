package matteo.blinkm.graphics;

import matteo.blinkm.Blinkm;
import matteo.blinkm.console.LineSelector;
import matteo.blinkm.console.LineSelector.Direction;

public class Matrix {

	public static char[] flatten(char[][] matrix, int rows, int cols) {
		char[] result = new char[rows*cols];
		for (int i=0; i<rows; i++) {
			for (int j=0; j<cols; j++) {
				result[i*10+j] = matrix[i][j];
			}
		}
		return result;
	}

	public static char[][] build(int rows, int cols) {
		char[][] matrix = new char[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				matrix[i][j] = (char) ((i*cols)+j+1);
				System.out.print(i +","+j+" "+ (int)matrix[i][j] + "|");
			}
			System.out.println();
			
		}
		return matrix;
	}

	public static char[] merge(char[]...parts) {
		int size = 0;
		for (char[] part : parts) {
			size += part.length;
		}
		char[] result = new char[size];
		int c = 0;
		for (int i=0; i<parts.length; i++) {
			for (int j=0; j<parts[i].length; j++) {
				result[c++] = parts[i][j];
			}
		}
		return result;
		
	}

	public static char[] square(char[][] matrix, int i, int j, int len) {
		char[] t = LineSelector.select(matrix, i, j, Direction.east, len);
		char[] r = LineSelector.select(matrix, i, j+len, Direction.south, len);
		char[] b = LineSelector.select(matrix, i+len, j+len, Direction.west, len);
		char[] l = LineSelector.select(matrix, i+len, j, Direction.north, len);
		char[] square = Matrix.merge(t, r, b, l);
		return square;
	}

}
