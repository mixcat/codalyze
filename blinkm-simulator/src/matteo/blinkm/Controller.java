package matteo.blinkm;

import matteo.blinkm.console.Commands;

public class Controller extends DeprecatedController {

	private static final int POS_COMMAND = 4;
	private static final int POS_ADDR = 1;
	private static final int POS_LEN = 2;
	private static final int POS_START_CMD = 4;
	private static final int MAX_CMD_LEN = Commands.COMMAND_LINE_LENGTH + Commands.COMMAND_ENVELOPE_LENGHT;
	private final Blinkm[] blinkms;
	private byte[] reminder;
	public Controller(Blinkm[] blinkms) {
		this.blinkms = blinkms;
	}

	public void receive(byte[] rcv) {
		byte[] data = ByteUtils.merge(reminder, rcv);
		reminder = null;
		int pos = 0;
		int length = -1;
		byte addr = -1;
		// max length of a command
		byte[] buf = new byte[MAX_CMD_LEN];
		ByteUtils.fill(buf, '$');
		for (int idx=0; idx < data.length; idx++) {
			byte curr = data[idx];
			buf[pos] = curr;			
			if (pos == POS_ADDR)
				addr = curr;
			if (pos == POS_LEN) {
				length = curr;
				// test for incomplete command
				if (idx+2+length <= data.length) {
					byte[] cmx = ByteUtils.slice(data, idx+2, length);
					dispatch(addr, cmx);
					idx+=(length+1);
					pos=0;
				}
				else {
					pos++;
				}
			}
			else {
				pos++;
			}
		}
		// if pos is positive, some chars have not been consumed
		// and will be prepended to next reeption
		if (pos > 0) {
			reminder = new byte[pos];
			for (int i=0;i<pos;reminder[i] = buf[i++]);
		}
	}
	
	protected void dispatch(byte addr, byte[] cmd) {
		if (addr == 0) {
			for (Blinkm b : blinkms)
				b.setCmd(cmd);
		}
		else {
			blinkms[addr].setCmd(cmd);
		}
	}

	public byte[] getReminder() {
		return reminder;
	}
}
