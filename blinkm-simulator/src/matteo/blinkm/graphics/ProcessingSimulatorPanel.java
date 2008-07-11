package matteo.blinkm.graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ProcessingSimulatorPanel {
	
	public JPanel getPanel() {
		JPanel frame = new JPanel();
		ProcessingSimulator processingSimulator = new ProcessingSimulator();
		frame.add(processingSimulator);
		//processingSimulator.setSize(600,600);
		frame.setSize(600,600);
		processingSimulator.setVisible(true);
		processingSimulator.init();
		processingSimulator.start();
		frame.setVisible(true);
		return frame;
	}
	
	public static void main(String[] args) {
		JPanel panel = new ProcessingSimulatorPanel().getPanel();
		JFrame frame = new JFrame();
		frame.add(panel);
		frame.setSize(panel.getSize());
		frame.setVisible(true);
	}
	
}
