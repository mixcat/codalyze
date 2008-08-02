package matteo.blinkm.gui;

import java.awt.Color;

import matteo.blinkm.Blinkm;
import matteo.blinkm.Controller;
import matteo.blinkm.console.Helper;
import matteo.blinkm.graphics.Cube;
import processing.core.PApplet;
import processing.net.Client;
import processing.net.Server;

@SuppressWarnings("serial")
public class ProcessingSimulator extends PApplet {

	private Blinkm[] leds;
	private Server server;
	private Controller controller;
	private final int rows;
	private final int cols;

	float edge = 20;
	float gap= edge*1.5F;
	private Cube[] cubes;
	
	public ProcessingSimulator(int sizeX, int sizeY, int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		size(sizeX,sizeY,P3D);
	}
	
	public void setServer(Server server) {
		this.server = server;
	}
	
	public void setup() {
		System.out.println("setup " + this);
		leds = new Blinkm[rows*cols];
		cubes = new Cube[100];
		server = new Server(this, Helper.DEFAULT_PORT);
		for (int i=0; i<leds.length; i++) {
			leds[i] = new Blinkm((byte)i);
			cubes[i] = new Cube(this, edge, edge, edge);
		}
		controller = new Controller(leds);
	}

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
			if ((i%rows) == 0) {
				tempY += gap;
				tempX = 0;
			}
			tempX += gap;
			translate(tempX, tempY, tempZ);
			fill(255,255,255);
			//PFont font;
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
