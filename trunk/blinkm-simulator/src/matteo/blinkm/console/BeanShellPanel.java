package matteo.blinkm.console;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.logging.Logger;

import javax.management.MBeanServerConnection;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import matteo.blinkm.Definition;
import matteo.blinkm.console.LineSelector.Direction;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.NameSource;
import bsh.NameSpace;
import bsh.util.NameCompletion;
import bsh.util.NameCompletionTable;

public class BeanShellPanel extends JPanel {
    
    /**
     * A logger for this class.
     **/
    private static final Logger LOG =
            Logger.getLogger(BeanShellPanel.class.getName());
    
    private MBeanServerConnection server;
    private final Interpreter interpreter;
    
    /** Creates a new instance of BeanShellPanel */
    public BeanShellPanel() {
        super(new GridLayout(1,0));

        final bsh.util.JConsole console = new bsh.util.JConsole();
        final NameCompletionTable nct = new NameCompletionTable();
        interpreter = new Interpreter(console); 
        nct.add(interpreter.getNameSpace());
        //NameSpace nameSpace = interpreter.getNameSpace();
        //nameSpace.
       // new NameCompletion
        console.setNameCompletion( nct );
        JScrollPane scrollPane = new JScrollPane(console);
        // Add the scroll pane to this panel.
        add(scrollPane);

        final Thread thread = new Thread( interpreter,"BeanShell" );
        thread.setDaemon(true);
	thread.start();
	
	try {
		interpreter.eval("import matteo.blinkm.console.Helper;");
		interpreter.eval("static import matteo.blinkm.console.Helper.*;");
		interpreter.eval("static import " + Definition.class.getCanonicalName() + ".*;");
		interpreter.eval("static import " + Direction.class.getCanonicalName() + ".*;");
		interpreter.eval("import *;");
		interpreter.eval("client.connect();");
	} catch (EvalError e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
        // Create the scroll pane and add the table to it.

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

        // Create the BeanShell Panel
        final BeanShellPanel shell = new BeanShellPanel();
        // Set up the MBeanServerConnection to the target VM
       // MBeanServerConnection server = connect(hostname, port);
       // shell.setMBeanServerConnection(server);

        // Create the standalone window with BeanShell panel
        // by the event dispatcher thread
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
