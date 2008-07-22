package matteo.blinkm.graphics;


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
	
	public static char[] line(char[][] matrix, int i, int j, Direction dir, int len) {
		char[] line = new char[len];
		for (int xi=0; xi<len; xi++) {
			int x = i+xi*dir.y;
			int y = j+xi*dir.x;
			System.out.print(x+","+y+" ");
			line[xi] = matrix[x][y];
		}
		System.out.println();
		return line;
	}

	public static char[] square(char[][] matrix, int i, int j, int len) {
		char[] t = Matrix.line(matrix, i, j, Direction.east, len-1);
		char[] r = Matrix.line(matrix, i, j+len-1, Direction.south, len-1);
		char[] b = Matrix.line(matrix, i+len-1, j+len-1, Direction.west, len-1);
		char[] l = Matrix.line(matrix, i+len-1, j, Direction.north, len-1);
		char[] square = Matrix.merge(t, r, b, l);
		return square;
	}

	public enum Direction {
		north(0,-1), east(+1,0), south(0,+1), west(-1,0);
	
		public final int x;
		public final int y;

		Direction (int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}