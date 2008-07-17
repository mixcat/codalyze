package matteo.blinkm.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class ProcessingSimulatorPanel {
	
	private ProcessingSimulator processingSimulator;

	public JPanel getPanel() {
		JPanel panel = new JPanel();
		panel.add(processingSimulator);
		//panel.setSize(600,600);
		//processingSimulator.setVisible(true);
		//processingSimulator.init();
		//processingSimulator.start();
		//panel.setVisible(true);
		return panel;
	}
	
	public static void main(String[] args) {
		JPanel panel = new ProcessingSimulatorPanel().getPanel();
		JFrame frame = new JFrame();
		frame.add(panel);
		frame.setSize(panel.getSize());
		frame.setVisible(true);
	}

	public void setSimulator(ProcessingSimulator processingSimulator) {
		this.processingSimulator = processingSimulator;
	}

	
}
