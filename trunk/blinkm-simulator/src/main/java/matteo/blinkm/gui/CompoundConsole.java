package matteo.blinkm.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import matteo.blinkm.console.Helper;

import org.w3c.dom.Element;

import cookxml.cookformlayout.CookFormLayoutLib;
import cookxml.cookswing.CookSwing;
import cookxml.core.DecodeEngine;
import cookxml.core.exception.CookXmlException;
import cookxml.core.exception.CreatorException;
import cookxml.core.interfaces.Creator;
import cookxml.core.interfaces.TagLibrary;
import cookxml.core.taglibrary.InheritableTagLibrary;

public class CompoundConsole {
	public static ProcessingSimulator simulator;
	public static void main(String[] args) throws Exception {
		final ConsolePanel consolePanel = new ConsolePanel();
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				getControlPanel();
				simulator.init();
			}
		});		
	}
	
	public ActionListener buttonAction = new ActionListener () {
		public void actionPerformed (ActionEvent e)
		{
			JOptionPane.showMessageDialog (null, e.getActionCommand ());
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
		public void focusGained(FocusEvent e) {
		}
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
	
	private static void getControlPanel() {
		InheritableTagLibrary tagLibrary = CookSwing.getSwingTagLibrary ();
		tagLibrary.setCreator("console", new Creator() {

			public Object create(String arg0, String arg1, Element arg2,
					Object arg3, DecodeEngine arg4) throws CreatorException {
				 return new ConsolePanel();
			}

			public Object editFinished(String arg0, String arg1, Element arg2,
					Object arg3, Object arg4, DecodeEngine arg5)
					throws CookXmlException {
				return arg4;
			}
			
		});
		tagLibrary.setCreator("simulator", new Creator() {

			public Object create(String arg0, String arg1, Element arg2,
					Object arg3, DecodeEngine arg4) throws CreatorException {
				ProcessingSimulatorPanel simulatorPanel = new ProcessingSimulatorPanel();
				simulator = new ProcessingSimulator(450, 450, 4, 4);
				Helper.rows = 4;
				Helper.cols = 4;
				simulatorPanel.setSimulator(simulator);
				return simulatorPanel.getPanel();
			}

			public Object editFinished(String arg0, String arg1, Element arg2,
					Object arg3, Object arg4, DecodeEngine arg5)
					throws CookXmlException {
				return arg4;
			}
		
		});
		CookFormLayoutLib.setupTags (tagLibrary);
		CookSwing cookSwing = new CookSwing (new CompoundConsole());		// just to for hooking up buttonAction
		File file = new File("src/main/resources/controlPanelLayout.xml");
		System.out.println(file.getAbsolutePath());
		Container container = cookSwing.render (file);
		container.setVisible(true);
		
	}
	
}
