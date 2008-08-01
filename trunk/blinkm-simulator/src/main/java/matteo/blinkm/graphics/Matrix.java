package matteo.blinkm.graphics;


public class Matrix {

	public static byte[] flatten(byte[][] matrix, int rows, int cols) {
		byte[] result = new byte[rows*cols];
		for (int i=0; i<rows; i++) {
			for (int j=0; j<cols; j++) {
				result[i*rows+j] = matrix[i][j];
			}
		}
		return result;
	}

	public static byte[][] build(int rows, int cols) {
		byte[][] matrix = new byte[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				matrix[i][j] = (byte) ((i*cols)+j+1);
				System.out.print(i +","+j+" "+ (int)matrix[i][j] + "|");
			}
			System.out.println();
			
		}
		return matrix;
	}

	public static byte[] merge(byte[]...parts) {
		int size = 0;
		for (byte[] part : parts) {
			size += part.length;
		}
		byte[] result = new byte[size];
		int c = 0;
		for (int i=0; i<parts.length; i++) {
			for (int j=0; j<parts[i].length; j++) {
				result[c++] = parts[i][j];
			}
		}
		return result;
		
	}
	
	public static byte[] line(byte[][] matrix, int i, int j, Direction dir, int len) {
		byte[] line = new byte[len];
		for (int xi=0; xi<len; xi++) {
			int x = i+xi*dir.y;
			int y = j+xi*dir.x;
			System.out.print(x+","+y+" ");
			line[xi] = matrix[x][y];
		}
		System.out.println();
		return line;
	}

	public static byte[] square(byte[][] matrix, int i, int j, int len) {
		byte[] t = Matrix.line(matrix, i, j, Direction.east, len-1);
		byte[] r = Matrix.line(matrix, i, j+len-1, Direction.south, len-1);
		byte[] b = Matrix.line(matrix, i+len-1, j+len-1, Direction.west, len-1);
		byte[] l = Matrix.line(matrix, i+len-1, j, Direction.north, len-1);
		byte[] square = Matrix.merge(t, r, b, l);
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
