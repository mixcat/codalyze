package matteo.blinkm.graphics;

import matteo.blinkm.Blinkm;
import matteo.blinkm.console.Command;

public class Controller {
	
	private final Blinkm[] blinkms;
	private char[] reminder;
	
	public Controller(Blinkm[] blinkms) {
		this.blinkms = blinkms;
	}
	
	public void dispatchReceivedCommands(char[] rcv) {
		char[] data = prepend(reminder, rcv);
		reminder = null;
		int pos = 0;
		Command blinkmCommand = null;
		
		char addr = 110;
		for (int idx=0; idx < data.length; idx++) {
			if (pos == 0) {
				addr = data[idx];
				if(addr < 0 || addr > 100)
					throw new RuntimeException("received address " + (int)addr+ ". Must be >= 0 and <= 100");
			}
			else if (pos == 1)
				blinkmCommand = new Command(data[idx]);
			else
				blinkmCommand.addPayload(data[idx]);

			if (blinkmCommand != null && blinkmCommand.isComplete()) {
				blinkmCommand.validate();
				if (addr == 0) {
					for(Blinkm blinkm : blinkms) {
						blinkm.setCmd(blinkmCommand);
					}
				}
				else {
					blinkms[(int)addr-1].setCmd(blinkmCommand);
				}
				blinkmCommand = null;
				pos = 0;
				continue;
			}
			pos++;
		}
		// if pos is positive, some chars have not been consumed
		// and will be prepended to next reeption
		if (pos > 0) {
			reminder = new char[pos];
			for (int i=0;i<pos;i++) {
				reminder[i] = data[data.length-pos+i];
			}
		}
	}
	
	public char[] prepend(char[] reminder, char[] rcv) {
		int rlen = (reminder != null) ? reminder.length : 0;
		char[] rt = new char[rlen + rcv.length];
		for (int i = 0; i < rlen; i++) {
			rt[i] = reminder[i];
		}
	
		for (int i=0;i<rcv.length;i++) {
			rt[rlen + i] = rcv[i];
		}
	
		return rt;
	}
	
}
