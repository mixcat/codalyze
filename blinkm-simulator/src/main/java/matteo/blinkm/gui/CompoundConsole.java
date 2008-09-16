package matteo.blinkm.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import matteo.blinkm.console.Helper;

import org.w3c.dom.Element;

import cookxml.cookformlayout.CookFormLayoutLib;
import cookxml.cookswing.CookSwing;
import cookxml.core.DecodeEngine;
import cookxml.core.exception.CookXmlException;
import cookxml.core.exception.CreatorException;
import cookxml.core.interfaces.Creator;
import cookxml.core.taglibrary.InheritableTagLibrary;

public class CompoundConsole {
	public ProcessingSimulator simulator;

	private ConsolePanel consolePanel;
	
	public static void main(String[] args) throws Exception {
		final CompoundConsole console = new CompoundConsole();
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				console.start();
			}
		});		
	}
	
	private void start() {
		consolePanel = new ConsolePanel();
		simulator = new ProcessingSimulator(450, 450, 9, 9);
		Helper helper = new Helper(simulator,9,9);
		consolePanel.setHelper(helper);
		InheritableTagLibrary tagLibrary = CookSwing.getSwingTagLibrary ();
		setupTags(tagLibrary);
		CookFormLayoutLib.setupTags (tagLibrary);
		CookSwing cookSwing = new CookSwing (this);		// just to for hooking up buttonAction
		File file = new File("src/main/resources/controlPanelLayout.xml");
		Container container = cookSwing.render (file);
		container.setVisible(true);
		simulator.init();
		consolePanel.setup();
	}

	private void setupTags(InheritableTagLibrary tagLibrary) {
		
		tagLibrary.setCreator("console", new Creator() {

			public Object create(String arg0, String arg1, Element arg2, Object arg3, DecodeEngine arg4) throws CreatorException {
				 return consolePanel;
			}

			public Object editFinished(String arg0, String arg1, Element arg2, Object arg3, Object arg4, DecodeEngine arg5) throws CookXmlException {
				return arg4;
			}
			
		});
		
		tagLibrary.setCreator("simulator", new Creator() {
			public Object create(String arg0, String arg1, Element arg2,Object arg3, DecodeEngine arg4) throws CreatorException {
				JPanel panel = new JPanel();
				panel.add(simulator);
				return panel;
			}

			public Object editFinished(String arg0, String arg1, Element arg2,Object arg3, Object arg4, DecodeEngine arg5) throws CookXmlException {
				return arg4;
			}
		
		});
	}
	
	public ActionListener buttonAction = new ActionListener () {
		public void actionPerformed (ActionEvent e)
		{
			JOptionPane.showMessageDialog (null, e.getActionCommand ());
			consolePanel.thread.interrupt();
		}
	};
	
	public ActionListener fadeSpeedListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			
		};
	};
	
	public ActionListener selectionAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String string = e.getActionCommand();
			String[] split = string.split(",");
			byte[] rt = new byte[split.length];
			for (int i = 0; i < rt.length; i++) {
				rt[i] = Byte.parseByte(split[i]);
				System.out.println("parsed byte " + Byte.parseByte(split[i]));
			}
			Helper.selectors.put("one", rt);
		}
	};
	
	public FocusListener selectionFocus = new FocusListener() {
		public void focusGained(FocusEvent e) {}
		public void focusLost(FocusEvent e) {
			String string = ((JTextField)e.getComponent()).getText();
			if (string == null || string.length() == 0)
				return;
			String[] split = string.split(",");
			byte[] rt = new byte[split.length];
			for (int i = 0; i < rt.length; i++) {
				try {
				rt[i] = Byte.parseByte(split[i]);
				System.out.println("parsed byte " + Byte.parseByte(split[i]));
				} catch (Exception ex) {
					return;
				}
			}
			Helper.selectors.put("one", rt);
		}
	};
	
}
