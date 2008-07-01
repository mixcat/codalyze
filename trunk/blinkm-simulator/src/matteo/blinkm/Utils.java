package matteo.blinkm;

public class Utils {
	public static String strCmd(char[] cmd) {
		String r = "";
		for (char c : cmd) {
			r += (int)c + ",";
		}
		return r;
	}
}
