package matteo.blinkm;

/**
 * Holds references to managed blinkm instances and dispatches commands to all
 * @author mcaprari
 *
 */
public class DeprecatedController {
	
	private Blinkm[] blinkms;
	private char[] reminder;
	
	private static char BROADCAST = 0;
	
	
	public void disxdpatchReceivedCommands(char[] rcv) {
		char[] data = prepend(reminder, rcv);
		reminder = null;
		int pos = 0;
		Command blinkmCommand = null;
		
		char addr = 110;
		for (int idx=0; idx < data.length; idx++) {
			if (pos == 0) {
				addr = data[idx];
				if(addr < 0 || addr > blinkms.length)
					throw new RuntimeException("received address " + (int)addr+ ". Must be >= 0 and <= " + blinkms.length);
			}
			else if (pos == 1)
				blinkmCommand = new Command(data[idx]);
			else
				blinkmCommand.addPayload(data[idx]);

			if (blinkmCommand != null && blinkmCommand.isComplete()) {
				blinkmCommand.validate();
				if (addr == BROADCAST) {
					for(Blinkm blinkm : blinkms) {
						//blinkm.setCmd(blinkmCommand);
					}
				}
				else {
					// address is a char > 0 (0 is broadcast), but
					// blinkm container array is 0 indexed
					int blinkmAddr = (int)addr-1;
					//blinkms[blinkmAddr].setCmd(blinkmCommand);
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
