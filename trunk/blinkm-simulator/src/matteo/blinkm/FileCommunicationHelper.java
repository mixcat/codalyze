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

	public char[] receiveData(char[] reminder) {
		char[] buf = new char[1000];
		try {
			int count=0;
			while(inputStream.available()>0 && count < buf.length) {				
				int read = inputStream.read();
				if (read != -1) {
					buf[count] = (char)read;
					count++;
				}
			}
			if (count == 0)
				return null;
			char[] rcv = new char[count];
			for(int i=0; i<count; i++) {
				rcv[i] = buf[i];
			}
			return rcv;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
