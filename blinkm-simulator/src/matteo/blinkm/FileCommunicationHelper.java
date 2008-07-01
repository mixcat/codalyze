package matteo.blinkm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileCommunicationHelper {

	private FileInputStream inputStream;
	
	public FileCommunicationHelper(File file) {
		try {
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public byte[] receiveData() {
		byte[] buf = new byte[1000];
		try {
			int count=0;
			while(inputStream.available()>0 && count < buf.length) {				
				int read = inputStream.read();
				if (read != -1) {
					buf[count] = (byte)read;
					count++;
				}
			}
			if (count == 0)
				return null;
			byte[] rcv = new byte[count];
			for(int i=0; i<count; i++) {
				rcv[i] = buf[i];
			}
			return rcv;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
