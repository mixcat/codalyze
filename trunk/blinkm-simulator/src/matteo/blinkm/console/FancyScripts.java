package matteo.blinkm.console;

import static matteo.blinkm.Definition.fadeToRGB;

import java.awt.Color;

public class FancyScripts {
	
	public static SimpleScript A = new SimpleScript()
	.line(150, fadeToRGB, Color.green)
	.line(200, fadeToRGB, Color.blue);
	
}
