package matteo.blinkm.gui;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.NameSource;
import bsh.util.NameCompletionTable;

import java.awt.BorderLayout;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import matteo.blinkm.Definition;
import matteo.blinkm.console.Commands;
import matteo.blinkm.console.Helper;
import matteo.blinkm.graphics.Matrix.Direction;

public class ConsolePanel extends JPanel {

	private final Interpreter interpreter;

	public ConsolePanel() {
		super(new BorderLayout());

		final bsh.util.JConsole console = new bsh.util.JConsole();
		final NameCompletionTable nct = new NameCompletionTable();
		interpreter = new Interpreter(console); 
		nct.add(interpreter.getNameSpace());
		nct.add(new ConfigurableNameSource(Helper.class));
		nct.add(new ConfigurableNameSource(Definition.class));
		console.setNameCompletion( nct );
		JScrollPane scrollPane = new JScrollPane(console);
		scrollPane.setSize(450,450);
		add(scrollPane);

		final Thread thread = new Thread( interpreter,"BeanShell" );
		thread.setDaemon(true);
		thread.start();

		try {
			interpreter.eval("import matteo.blinkm.console.Helper;");
			interpreter.eval("static import matteo.blinkm.console.Helper.*;");
			interpreter.eval("static import " + Definition.class.getCanonicalName() + ".*;");
			interpreter.eval("static import " + Direction.class.getCanonicalName() + ".*;");
			interpreter.eval("static import " + Commands.class.getCanonicalName() + ".*;");
			interpreter.eval("import *;");
			//interpreter.eval("client.connect();");
		} catch (EvalError e) {
			e.printStackTrace();
		}

	}

	public class ConfigurableNameSource implements NameSource {

		private ArrayList<String> allNames = new ArrayList<String>();

		public ConfigurableNameSource(Class clazz) {
			extract(clazz);
		}
		
		private void extract(Class clazz) {
			if (clazz.isEnum()) {
				enumNames(clazz.getEnumConstants());
			}
			memberNames(clazz.getDeclaredFields());
			memberNames(clazz.getMethods());
		}
		
		private void enumNames(Object[] enumConstants) {
			for (Object object : enumConstants) {
				allNames.add(object.toString());
			}
		}
		private void memberNames(Member[] members) {
			for (Member member : members) {
				if (Modifier.isPublic(member.getModifiers())) 
					allNames.add(member.getName());
			}
			
		}

		public String[] getAllNames() {
			return allNames.toArray(new String[allNames.size()]);
		}

		public void addNameSourceListener(Listener arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	public static void main(String[] args) throws Exception {
		/*
        // Validate the input arguments
        if (args.length != 1) {
            usage();
        }

        String[] arg2 = args[0].split(":");
        if (arg2.length != 2) {
            usage();
        }
        String hostname = arg2[0];
        int port = -1;
        try {
            port = Integer.parseInt(arg2[1]);
        } catch (NumberFormatException x) {
            usage();
        }
        if (port < 0) {
            usage();
        }
		 */

		final ConsolePanel shell = new ConsolePanel();
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				createAndShowGUI(shell);
			}
		});

	}

	private static void usage() {
		System.out.println("Usage: java BeanShellPanel <hostname>:<port>");
		System.exit(1);
	}


	private static void createAndShowGUI(JPanel bsp) {
		// Create and set up the window.
		JFrame frame = new JFrame("BeanShell");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		JComponent contentPane = (JComponent) frame.getContentPane();
		contentPane.add(bsp, BorderLayout.CENTER);
		contentPane.setOpaque(true); //content panes must be opaque
		contentPane.setBorder(new EmptyBorder(12, 12, 12, 12));
		frame.setContentPane(contentPane);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

}
