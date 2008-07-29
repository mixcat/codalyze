#ifndef _PROTOCOLFUNCS_H
#define _PROTOCOLFUNCS_H

#include "defines.h"


int blinkm_getcmdlen (int cmd) {

	switch(cmd) {
		case CMD_GO_TO_RGB: 
			return 3;
		case CMD_FADE_TO_RGB: 
			return 3;
		case CMD_FADE_TO_HSB: 
			return 3;
		case FADE_TO_RANDOM_RGB: 
			return 3;
		case FADE_TO_RANDOM_HSB: 
			return 3;
		case CMD_PLAY_SCRIPT: 
			return 3;
		case CMD_STOP_SCRIPT: 
			return 0;
		case CMD_SET_FADE_SPEED: 
			return 1;
		case CMD_SET_TIME_ADJUST: 
			return 1;
		case CMD_GET_CURRENT_RGB: 
			return 3;
		case CMD_WRITE_SCRIPT_LINE: 
			return 7;
		case CMD_READ_SCRIPT_LINE: 
			return 2;
		case CMD_SET_SCRIPT_LR: 
			return 3;
		case CMD_SET_ADDRESS: 
			return 4;
		case CMD_GET_ADDRESS: 
			return 0;
		case GET_FIRMWARE_VERSION: 
			return 0;
		case SET_STARTUP_PARAMS: 
			return 5;
		case CMD_SLEEP:
			return 2;
	}
	return -1;
}




void arduino_playbackmsg(struct queue_t *q, int executiondelay, boolean transmit) {

	// playback with delay
	if ( Serial.available() > 0 )
	{
		// in sec
		int delaysec = Serial.read();
		delay(delaysec*1000);
	}


	struct element_t* el;

	while ( (el = pop(q)) )
	{
		if ( el == NULL )
			break;


		// if it is false, messages will just be free()'d
		if ( transmit == true ) 
		{     
			if ( el->payload[0] == CMD_SLEEP ) {
				delay(el->payload[1] * el->payload[2]);  
				// we don't want to send on the wire an invalid command
				goto blasphemy;
			}

			Wire.beginTransmission(el->addr);
			Wire.send(el->payload, el->len);
			Wire.endTransmission();
		}

		delay(executiondelay);

		// thou hast saith the 'g' word!
blasphemy:
		free(el->payload);
		free(el);
	}
	q->head = NULL;
	q->tail = NULL;  
}  // arduino_playbackmsg()




void arduino_erasemsgqueue (struct queue_t *q) {
	arduino_playbackmsg(q, 0, false);
}




void arduino_recordmsg(struct queue_t *q) {
	digitalWrite(recordingPin, HIGH);
	boolean keeprecording = true;
	DBG(Serial.println("begin recording..."););

	do {

		// wait for some data: at least address and command
		while ( Serial.available() < 2 )
			;

		int addr, cmd;

		{
			// we've got data here
			int first = Serial.read();
			int second = Serial.read();

			// check for exit condition  
			if ( first == ARDUINO_MSG && second == ARDUINO_RECORD_MSG )
				break;   // could also be keeprecording=false + break, if you fancy

			addr = first;
			cmd = second;
		}

		int cmdlen = blinkm_getcmdlen(cmd);

		// negative value means command not recognized
		if ( cmdlen < 0 )
		{
			DBG(Serial.println("command not recognized"););
			errorblink_cmd();
			/* FIXME: for now we ignore it */
			return;
		}

		/* wait until we have all data we need */
		while ( Serial.available() < cmdlen )
			;

		byte* payload = (byte*) malloc(sizeof(byte) * (cmdlen+1) );
		if ( payload == NULL )
		{
			DBG(Serial.println("Cannot malloc for payload!"););
			errorblink_mem();
			// flush mem
			arduino_erasemsgqueue(q);
			return;
		}

		payload[0] = cmd;
		DBG(Serial.println("Payload received:"););
		for ( int i = 0 ; i < cmdlen ; i++ )
		{
			payload[i+1] = Serial.read();
			DBG(Serial.print(payload[i+1], DEC););
		}
		DBG(Serial.println(););

		/* done with reading, flush before pushing into queue */
		Serial.flush();

		/* save the packet */  
		if ( push(q, addr, payload, cmdlen+1) < 0 )
		{
			errorblink_mem();
			DBG(Serial.println("cannot malloc for push!"););
			// flush mem
			arduino_erasemsgqueue(q);
			return;
		}
	} while ( keeprecording == true);

	DBG(Serial.println("end recording"););
	digitalWrite(recordingPin, LOW);
}  // arduino_recordmsg()


/* set the delay between commands */
void arduino_setdelay(int* executiondelay) {

	// playback with delay
	while ( Serial.available() < 2 )
		;

	int delay1 = Serial.read();
	int delay2 = Serial.read();

	//executiondelay = map(executiondelay, 0, 255, 0, 10000);
	*executiondelay = delay1*delay2;

}

#endif
