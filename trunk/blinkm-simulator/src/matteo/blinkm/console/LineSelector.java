package matteo.blinkm.console;

public class LineSelector {

	
	
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
