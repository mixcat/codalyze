package matteo.blinkm.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

public class CompoundConsole {
	public static ProcessingSimulator simulator;
	public static void main(String[] args) throws Exception {
		ProcessingSimulatorPanel simulatorPanel = new ProcessingSimulatorPanel();
		simulator = new ProcessingSimulator(450, 450);
		simulatorPanel.setSimulator(simulator);
		final JPanel panel = simulatorPanel.getPanel();
		
		final ConsolePanel consolePanel = new ConsolePanel();
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				createAndShowGUI(consolePanel, panel);
				simulator.init();
			}
		});		
	}
	
	private static void createAndShowGUI(JPanel consolePanel, JPanel simulatorPanel) {
		// Create and set up the window.
		JFrame frame = new JFrame("Blinkm Processing Console");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Create and set up the content pane.
		JComponent contentPane = (JComponent) frame.getContentPane();
		contentPane.setSize(1200, 550);
		consolePanel.setSize(450,450);
		contentPane.setLayout(new BorderLayout());
		contentPane.add(simulatorPanel, BorderLayout.WEST);
		contentPane.add(consolePanel, BorderLayout.CENTER);
		contentPane.setOpaque(true); //content panes must be opaque
		contentPane.setBorder(new LineBorder(Color.black));
		frame.setContentPane(contentPane);
		// Display the window.
		frame.pack();
		frame.setSize(1200, 550);
		frame.setVisible(true);
	}
}
