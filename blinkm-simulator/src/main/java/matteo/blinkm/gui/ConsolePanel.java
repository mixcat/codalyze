package matteo.blinkm.gui;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.NameSource;
import bsh.util.NameCompletionTable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import matteo.blinkm.ByteUtils;
import matteo.blinkm.Definition;
import matteo.blinkm.console.Commands;
import matteo.blinkm.console.Helper;
import matteo.blinkm.graphics.Matrix;
import matteo.blinkm.graphics.Matrix.Direction;

@SuppressWarnings("serial")
public class ConsolePanel extends JPanel {


	private Helper helper;
	private Interpreter interpreter;
	private bsh.util.JConsole console;
	public Thread thread;

	public ConsolePanel() {
		super(new BorderLayout());

		console = new bsh.util.JConsole();
		interpreter = new Interpreter(console); 
		JScrollPane scrollPane = new JScrollPane(console);
		scrollPane.setSize(450,450);
		add(scrollPane);

		thread = new Thread( interpreter,"BeanShell" );
		thread.setDaemon(true);
		thread.start();
	}
	
	public void setup() {
		NameCompletionTable nct = new NameCompletionTable();
		nct.add(interpreter.getNameSpace());
		nct.add(new ConfigurableNameSource(Helper.class));
		nct.add(new ConfigurableNameSource(Definition.class));
		console.setNameCompletion( nct );
		importClass(Definition.class, true);
		importClass(Direction.class, true);
		importClass(Direction.class, false);
		importClass(Commands.class, true);	
		importClass(Commands.class, false);
		importClass(Commands.class, true);	
		importClass(Color.class, true);
		importClass(Helper.class, true);
		importClass(Matrix.class, false);
		importClass(ByteUtils.class, true);
		try {
			interpreter.set("H", helper);
			interpreter.source("src/main/resources/console/init-console.bsh");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void importClass(Class clazz, boolean isStatic) {
		String def = isStatic ? "static import " : "import ";
		def += clazz.getCanonicalName();
		def += isStatic ? ".*;" : ";";
		try {
			interpreter.eval(def);
		} catch (EvalError e) {
			throw new RuntimeException(e);
		}
	}

	public class ConfigurableNameSource implements NameSource {

		private ArrayList<String> allNames = new ArrayList<String>();

		@SuppressWarnings("unchecked")
		public ConfigurableNameSource(Class clazz) {
			extract(clazz);
		}
		
		@SuppressWarnings("unchecked")
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

	public void setHelper(Helper helper) {
		this.helper = helper;
	}

}
