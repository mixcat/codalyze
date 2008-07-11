package matteo.blinkm.graphics;


import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import matteo.blinkm.Blinkm;
import processing.core.PApplet;
import processing.net.Client;
import processing.net.Server;
import bsh.util.JConsole;

public class ProcessingSimulator extends PApplet {

	private Blinkm[] leds;
	private Server server;
	private Controller controller;

	public void setup() {
		size(600,600,P3D);
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
	float gap= edge;//*1.5F;
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
			Color color = leds[i].getColor();
			cubes[i].draw(color);
			popMatrix();
		}
	}
	
	

	public static void xmain(String[] args) {
		
		System.out.println("uuu");
		//bsh.Interpreter.main(args);
		
		JFrame cframe = new JFrame();
		JPanel panel = new JPanel();
		cframe.add(panel);
		JConsole console = new JConsole();
		panel.add(console);
		panel.setSize(600,600);
		console.setSize(600,600);
		panel.setSize(600,600);
		cframe.setSize(600,600);
		//console.setVisible(true);
		//panel.setVisible(true);
		cframe.setVisible(true);
		//Interpreter interpreter = new Interpreter( console );
		//new Thread( interpreter ).start(); // start a thread to call the run() method
		//Interpreter interpreter = new Interpreter(console);
	
	}


}
