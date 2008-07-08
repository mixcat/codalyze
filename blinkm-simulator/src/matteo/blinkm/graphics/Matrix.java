package matteo.blinkm.graphics;

import matteo.blinkm.Blinkm;

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

}
