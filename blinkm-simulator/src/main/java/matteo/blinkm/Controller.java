package matteo.blinkm;

public class Controller {

	private static final int POS_ADDR = 0;
	private static final int POS_CMD = 1;
	private final Blinkm[] blinkms;
	public Controller(Blinkm[] blinkms) {
		this.blinkms = blinkms;
	}

	int pos = 0;
	int len = -1;
	byte addr;
	byte[] buf;
	public void receive(byte[] rcv) {
		for(int i=0; i<rcv.length; i++) {
		    switch(pos) {
		    case POS_ADDR:
		    addr = rcv[i];
		      break;
		    case POS_CMD: // this is cmd, get len and transmit
		      len = Definition.getByChar(rcv[i]).getNumArgs();
		      buf = new byte[len+1];
		      buf[0] = rcv[i];
		      break;
		    default:
		      buf[pos-1] = rcv[i];
		    }

		    if (len != -1 && pos == len+1) {
		      dispatch(addr, buf);
		      len = -1;
		      pos = 0;
		    }
		    else {
		      pos++;
		    }
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

}
