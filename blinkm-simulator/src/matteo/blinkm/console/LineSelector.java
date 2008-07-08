package matteo.blinkm.console;

public class LineSelector {

	public static char[] select(char[][] matrix, int i, int j, Direction dir, int len) {
		char[] line = new char[len];
		for (int xi=0; xi<len; xi++) {
			System.out.print(xi +":" + xi*dir.y + ":" + j+xi*dir.x + ":" +(int)matrix[i+xi*dir.y][j+xi*dir.x] + " -- ");
			line[xi] = matrix[i+xi*dir.y][j+xi*dir.x];
		}
		System.out.println();
		return line;
	}
	
	public enum Direction {
		north(-1,0), east(0,+1), south(+1,0), west(0,-1);
	
		final int x;
		final int y;

		Direction (int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}
