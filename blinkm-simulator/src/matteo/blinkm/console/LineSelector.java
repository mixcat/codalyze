package matteo.blinkm.console;

public class LineSelector {

	public static char[] select(char[][] matrix, int i, int j, Direction dir, int len) {
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
	
	public enum Direction {
		north(0,-1), east(+1,0), south(0,+1), west(-1,0);
	
		final int x;
		final int y;

		Direction (int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}
