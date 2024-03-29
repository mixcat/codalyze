package matteo.blinkm.graphics;

import java.awt.Color;


import processing.core.PApplet;

public 	class Cube {
	Point3D[] vertices = new Point3D[24];
	float w, h, d;
	private final PApplet applet;

	public void draw(Color color) {
		applet.fill(color.getRGB());
		for (int i=0; i<6; i++){
			applet.beginShape(PApplet.QUADS);
			for (int j=0; j<4; j++){
				applet.vertex(vertices[j+4*i].x, vertices[j+4*i].y, vertices[j+4*i].z);
			}
			applet.endShape();
		}		
	}
	
	void draw(byte[] RGB){

	}

	public Cube(PApplet pa, float w, float h, float d){
		this.applet = pa;
		this.w = w;
		this.h = h;
		this.d = d;

		// cube composed of 6 quads
		//front
		vertices[0] = new Point3D(-w/2,-h/2,d/2);
		vertices[1] = new Point3D(w/2,-h/2,d/2);
		vertices[2] = new Point3D(w/2,h/2,d/2);
		vertices[3] = new Point3D(-w/2,h/2,d/2);
		//left
		vertices[4] = new Point3D(-w/2,-h/2,d/2);
		vertices[5] = new Point3D(-w/2,-h/2,-d/2);
		vertices[6] = new Point3D(-w/2,h/2,-d/2);
		vertices[7] = new Point3D(-w/2,h/2,d/2);
		//right
		vertices[8] = new Point3D(w/2,-h/2,d/2);
		vertices[9] = new Point3D(w/2,-h/2,-d/2);
		vertices[10] = new Point3D(w/2,h/2,-d/2);
		vertices[11] = new Point3D(w/2,h/2,d/2);
		//back
		vertices[12] = new Point3D(-w/2,-h/2,-d/2); 
		vertices[13] = new Point3D(w/2,-h/2,-d/2);
		vertices[14] = new Point3D(w/2,h/2,-d/2);
		vertices[15] = new Point3D(-w/2,h/2,-d/2);
		//top
		vertices[16] = new Point3D(-w/2,-h/2,d/2);
		vertices[17] = new Point3D(-w/2,-h/2,-d/2);
		vertices[18] = new Point3D(w/2,-h/2,-d/2);
		vertices[19] = new Point3D(w/2,-h/2,d/2);
		//bottom
		vertices[20] = new Point3D(-w/2,h/2,d/2);
		vertices[21] = new Point3D(-w/2,h/2,-d/2);
		vertices[22] = new Point3D(w/2,h/2,-d/2);
		vertices[23] = new Point3D(w/2,h/2,d/2);
	}

}
