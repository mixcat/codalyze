package matteo.blinkm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BlinkmController {
	
	public static void main(String[] args) throws IOException {
	
		FileOutputStream outputStream = new FileOutputStream(new File("c:\\blinkm.txt"),true);
		
		byte[][] matrix = new byte[100][];
		for (int i=0; i<100; i++) {
			byte b = (byte)i;
			byte[] cmd = new byte[] { b, 0, 0, 0 };
			matrix[i] = cmd;
		}
		
		for (int i=0; i<matrix.length; i++) {
			outputStream.write(matrix[i]);
		}
		outputStream.flush();
		//outputStream.close();
		
		int target = 0;
		byte[] black = new byte[] { 0, 0, 0, 0 };
		byte[] yellow = new byte[] { 0, 127, 127, 0 }; 
		while (true) {
			int previous = 0;
			if (target==100) 
				target = 0;
			
			if (target == 0)
				previous = 99;
			else
				previous = target-1;
			yellow[0] = (byte) target;
			black[0] = (byte)previous;
			outputStream.write(black);
			outputStream.write(yellow);
			outputStream.flush();
			
			target++;
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
