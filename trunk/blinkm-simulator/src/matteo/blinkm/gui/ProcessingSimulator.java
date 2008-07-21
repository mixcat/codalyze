package matteo.blinkm.gui;

import java.awt.Color;

import matteo.blinkm.Blinkm;
import matteo.blinkm.Controller;
import matteo.blinkm.DeprecatedController;
import matteo.blinkm.graphics.Cube;
import processing.core.PApplet;
import processing.core.PFont;
import processing.net.Client;
import processing.net.Server;

public class ProcessingSimulator extends PApplet {

	private Blinkm[] leds;
	private Server server;
	private Controller controller;

	public ProcessingSimulator(int sizeX, int sizeY) {
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

	private byte[] receiveData() {
		Client client = server.available();
		if (client != null) {
			return client.readBytes();
		}
		return null;
	}

	public void draw() {
		byte[] rcv = receiveData();
		if (rcv != null) {
			controller.receive(rcv);
		}
		
		background(100,125,200);  
		translate(50, 50, 0);
		float tempY = 0, tempX = 0, tempZ = gap*2;
		for (int i=0; i<leds.length; i++) {
			pushMatrix();
			if ((i%10) == 0) {
				tempY += gap;
				tempX = 0;
			}
			tempX += gap;
			translate(tempX, tempY, tempZ);
			fill(255,255,255);
			PFont font;
			// The font must be located in the sketch's 
			// "data" directory to load successfully
			//font = loadFont("FFScala-32.vlw"); 
			//textFont(font, 32); 
			//text("word", 15, 50);
			leds[i].tick();
			Color color = leds[i].getColor();
			cubes[i].draw(color);
			popMatrix();
		}
	}
}
