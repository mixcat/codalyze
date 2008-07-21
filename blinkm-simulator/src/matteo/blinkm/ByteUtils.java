package matteo.blinkm;

import java.awt.Color;

public class ByteUtils {
	
	public static byte[] slice(byte[] src, int start, int len) {
		byte[] rt = new byte[len];
		for (int i=0; i<len; i++) {
			rt[i] = src[start+i];
		}
		return rt;
	}
	
	public static Color getColor(byte r, byte g, byte b) {
		return new Color( (int)r&0xff, (int)g&0xff, (int)b&0xff);
	}

	public static Color getHSBColor(byte b, byte c, byte d) {
		return Color.getHSBColor(b&0xff, c&0xff, d&0xff);
	}
	
	public static byte[] unpad(byte[] bs, int len) {
		if (bs.length <= len)
			return bs;
		byte[] rt = new byte[len];
		for (int i=0; i<rt.length; i++)
			rt[i] = bs[i];
		return rt;
	}
	
	public static byte[] merge(byte[] reminder, byte[] rcv) {
		int rlen = (reminder != null) ? reminder.length : 0;
		byte[] rt = new byte[rlen + rcv.length];
		for (int i = 0; i < rlen; i++) {
			rt[i] = reminder[i];
		}
	
		for (int i=0;i<rcv.length;i++) {
			rt[rlen + i] = rcv[i];
		}
	
		return rt;
	}

	public static void fill(byte[] buf, char c) {
		for (int i = 0; i < buf.length; buf[i++] = (byte) c);
	}
	
	public static byte[][] split(byte[] src, int pos) {
		byte[][] rt = new byte[2][];
		rt[0] = slice(src, 0, pos);
		rt[1] = slice(src, pos, src.length-pos);
		return rt;
	}
}
