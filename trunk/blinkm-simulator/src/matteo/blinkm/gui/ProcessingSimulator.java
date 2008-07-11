package matteo.blinkm.gui;

import java.awt.Color;

import matteo.blinkm.Blinkm;
import matteo.blinkm.Controller;
import matteo.blinkm.graphics.Cube;
import processing.core.PApplet;
import processing.net.Client;
import processing.net.Server;

public class ProcessingSimulator extends PApplet {

	private Blinkm[] leds;
	private Server server;
	private Controller controller;
	private final int sizeX;
	private final int sizeY;

	public ProcessingSimulator(int sizeX, int sizeY) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		size(sizeX,sizeY,P3D);
	}
	
	public void setup() {
		
		System.out.println("setup " + this);
		server = new Server(this, 5204); 
		leds = new Blinkm[100];
		cubes = new Cube[100];
		for (int i=0; i<leds.length; i++) {
			leds[i] = new Blinkm((byte)i);
			cubes[i] = new Cube(this, edge, edge, edge);
		}
		controller = new Controller(leds);
	}

	float edge = 20;
	float gap= edge*1.5F;
	private Cube[] cubes;

	private char[] receiveData() {
		Client client = server.available();
		if (client != null) {
			char[] rcv = new char[client.available()];
			int i = 0;
			while (client.available()>0 && i<rcv.length) {
				rcv[i++] = client.readChar();
			}
			return rcv;
		}
		return null;
	}

	public void draw() {
		char[] rcv = receiveData();
		if (rcv != null) {
			System.out.println("dispatching buf " + rcv.length);
			controller.dispatchReceivedCommands(rcv);
		}

		background(100,125,200);  
		translate(50, 50, 0);
		float tempX = 0, tempY = 0, tempZ = gap*2;
		for (int i=0; i<leds.length; i++) {
			pushMatrix();
			if ((i%10) == 0) {
				tempX += gap;
				tempY = 0;
			}
			tempY += gap;
			translate(tempX, tempY, tempZ);
			fill(255,255,255);
			Color color = leds[i].getColor();
			cubes[i].draw(color);
			popMatrix();
		}
	}
}
