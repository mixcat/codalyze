package matteo.blinkm;

import processing.core.PApplet;
import processing.serial.Serial;


public class BlinkMComm {
	private int portSpeed = 19200;
	private Serial port;

	public void connect( PApplet p, String portname ) throws Exception {
		if(port != null)
			port.stop(); 
		port = new Serial(p, portname, portSpeed);
	}

	public void disconnect() {
		if( port!=null )
			port.stop();
	}

	public synchronized void send( byte[] cmd ) {
		port.write(cmd);
	}
	
	public synchronized byte[] read() {
		if (port.available() > 0)
			return port.readBytes();
		return null;
	}

}


