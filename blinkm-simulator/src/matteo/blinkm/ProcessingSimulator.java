package matteo.blinkm;


import java.io.File;

import processing.core.PApplet;


public class ProcessingSimulator extends PApplet {

	static final int PACKET_SIZE = 9;
	static final int ADDR_LEN = 1;
	
	Blinkm[] leds;
	byte addr;
	char[] cmd;
	
	MemoryCommunicationHelper comm;
	public void setup() {
		super.frameRate(60);
		//comm = new FileCommunicationHelper(new File("C:\\blinkm.txt"));
		comm = new MemoryCommunicationHelper();
		leds = new Blinkm[100];
		cmd = new char[PACKET_SIZE-ADDR_LEN];
		for (int i=0; i<leds.length; i++) {
			leds[i] = new Blinkm((byte)i, new Cube(this, edge, edge, edge));
		}
		size(600,600,P3D);
	}

	float edge = 20;
	float gap= edge*1.5F;
	long drawCount = 0;
	public void draw() {
		char[] rcv = comm.receiveData();
		if (rcv != null) {
			dispatchReceivedCommands(leds, rcv, cmd);
		}

		background(100,125,200);  
		translate(200, height/4, 0);
		float tempX = 0, tempY = 0, tempZ = gap*2;
		for (int i=0; i<leds.length; i++) {
			pushMatrix();
			if ((i%10) == 0) {
				tempX += gap;
				tempY = 0;
			}
			tempY += gap;
			translate(tempX, tempY, tempZ);
			fill(0,0,0);
			leds[i].draw(drawCount);
			popMatrix();
		}
		
		drawCount++;
	}
	
	/**
	 * Cuts received data into commands and dispatches them to blinkms.
	 * 
	 * @param receivedData
	 */
	private void dispatchReceivedCommands(Blinkm[] blinkms, char[] receivedData, char[] command) {
		for (int idx=0; idx<receivedData.length; idx++) {
			if (idx%PACKET_SIZE == 0) {
				addr = (byte) receivedData[idx]; 
			}
			else {
				command[(idx%PACKET_SIZE)-ADDR_LEN] = receivedData[idx];
			}

			if (idx%PACKET_SIZE == PACKET_SIZE-1) {
				if (addr == 0) {
					println("applying broadcast command: " + Utils.strCmd(command));
					for(Blinkm blinkm : blinkms) {
						blinkm.setCmd(command);
					}
				}
				else {
					blinkms[(int)addr].setCmd(command);
				}
			}
		}
	}
}
