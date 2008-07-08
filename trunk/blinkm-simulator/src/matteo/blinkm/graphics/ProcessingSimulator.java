package matteo.blinkm.graphics;


import java.io.File;
import java.util.ArrayList;

import matteo.blinkm.Blinkm;
import matteo.blinkm.Definition;
import matteo.blinkm.FileCommunicationHelper;
import matteo.blinkm.MemoryCommunicationHelper;
import matteo.blinkm.console.Command;
import processing.core.PApplet;
import processing.net.*;

public class ProcessingSimulator extends PApplet {

	static final int PACKET_SIZE = 9;
	static final int ADDR_LEN = 1;

	Blinkm[] leds;
	byte addr;
	char[] cmd;

	//ProcessingSimulatorServer comm;
	private Server server;
	public void setup() {
		size(600,600,P3D);
		System.out.println("setup " + this);
		server = new Server(this, 5204); 
		leds = new Blinkm[100];
		cmd = new char[PACKET_SIZE-ADDR_LEN];
		for (int i=0; i<leds.length; i++) {
			leds[i] = new Blinkm((byte)i, new Cube(this, edge, edge, edge));
		}
	}

	float edge = 20;
	float gap= edge*1.5F;
	long drawCount = 0;
	char[] reminder = new char[0];

	private char[] receiveData() {
		Client client = server.available();
		if (client != null) {
			int add = 0;
			if (reminder != null && reminder.length>0)
				add = reminder.length;
			char[] rt = new char[add + client.available()];
			int i = 0;
			while (client.available()>0 && i<rt.length) {
				rt[add + i++] = client.readChar();
			}
			if (add > 0) {
				for (int j = 0; j < reminder.length; j++) {
					rt[j] = reminder[j];
				}
			}
			System.out.println("dispatching buf " + rt.length);
			reminder = dispatchReceivedCommands(leds, rt, cmd);
		}
		return null;
	}

	public void draw() {
		receiveData();
		

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
	 * @param rcv
	 * @return 
	 */
	private char[] dispatchReceivedCommands(Blinkm[] blinkms, char[] rcv, char[] command) {
		int pos = 0;
		Command blinkmCommand = null;
		char[] reminder = null;
		for (int idx=0; idx < rcv.length; idx++) {
			if (pos == 0)
				addr = (byte) rcv[idx];
			else if (pos == 1)
				blinkmCommand = new Command(rcv[idx]);
			else
				blinkmCommand.addPayload(rcv[idx]);

			if (blinkmCommand != null && blinkmCommand.isComplete()) {
				if (addr == 0) {
					for(Blinkm blinkm : blinkms) {
						blinkm.setCmd(blinkmCommand);
					}
				}
				else {
					blinkms[(int)addr-1].setCmd(blinkmCommand);
				}
				pos = 0;
				continue;
			}
			pos++;
		}
		// if pos is positive, some chars have not been consumed
		// and will be prepended to next reeption
		if (pos > 0) {
			reminder = new char[pos];
			for (int i=0;i<pos;i++) {
				reminder[i] = rcv[rcv.length-pos+i];
			}
			return reminder;
		}
		return null;
	}
}
