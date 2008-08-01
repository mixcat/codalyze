/*
 * BlinkMFastComm -- Communication gateway between Blinkms on i2c bus
 *
 * Command format is:
 * <i2c_addr><cmd_byte>[<send_byte0>...]
 * 
 * 2008, Matteo Caprari
 */

#ifndef _BLINKMFASTCOMM_LORY
#define _BLINKMFASTCOMM_LORY

#include "defines.h"
#include "errorfuncs.h"


#include "Wire.h"
#include "blinkm_funcs.h"
#include "queue_funcs.h"
#include "protocol_funcs.h"

struct queue_t q;
int executiondelay;

void setup() {

	// set output pin
	pinMode(recordingPin, OUTPUT);

	// init blinkm
	if( BLINKM_INIT_POWERED )
		BlinkM_beginWithPower();
	else
		BlinkM_begin();

	Serial.begin(19200); 
	Serial.println("BlinkMCommander ready");

	// queue init
	q.head = NULL;
	q.tail = NULL;

	executiondelay = 0;

 	errorblink_mem();
	delay(1000);
	errorblink_cmd();

}  // setup()


void loop() {  

	/* wait for some data, please */
	if ( Serial.available() <= 0 )
		return;

	// now we have data
	int first = Serial.read();

	/* we have to check if the first byte is the beginning of a special command */
	switch ( first )
	{
		case ARDUINO_MSG:
			cmd_board();
			break;
		default:
			cmd_blinkm(first);
	}

}  // loop()


/**************************/
/* blinkm real-time usage */

void cmd_blinkm(int addr) {
	DBG(Serial.println("realtime mode enabled"););

	/* wait for data: the command to be sent to the blinkm */
	while ( Serial.available() <= 0 )
		;

	int cmd = Serial.read();
	int cmdlen = blinkm_getcmdlen(cmd);

	// negative value means command not recognized
	if ( cmdlen < 0 )
	{
		errorblink_cmd();
		DBG(
				Serial.print("Command ");
				Serial.print(cmd);
				Serial.println(" not recognized in quick mode");
		   );
		return;
	}

	/* wait until we have all data we need */
	while ( Serial.available() < cmdlen )
		;

	/* first two bytes for address + command */
	byte* full_command = (byte*) malloc(sizeof(byte) * ( 1 + cmdlen) );
	if ( full_command == NULL )
	{
		errorblink_mem();
		DBG(Serial.println("Cannot malloc for payload!"););
		free(full_command);
		return;
	}
	full_command[0] = cmd;

	for ( int i = 0 ; i < cmdlen ; i++ )
		full_command[i+1] = Serial.read();

	/* done with reading, flush before sending out */
	Serial.flush();

	/* send the full command to the specified address */
	Wire.beginTransmission(addr);
	Wire.send(full_command, 1+cmdlen);
	Wire.endTransmission();

	free(full_command);
}  // cmd_blinkm()


/***************************/
/* internal board commands */

void cmd_board() {  
	DBG(Serial.println("Board command mode enabled"););
	int internal_cmd;

	// beware: this will loop forever until the user sends a byte 
	while ( Serial.available() <= 0 )
		;      
	internal_cmd = Serial.read();

	switch (internal_cmd)
	{
		case ARDUINO_RECORD_MSG:
			arduino_recordmsg(&q);
			break;
		case ARDUINO_PLAYBACK_MSG:
			// third parameter indicate whether message is to be transmitted or not
			arduino_playbackmsg(&q, executiondelay, true);
			break;
		case ARDUINO_ERASE_MSG:
			arduino_erasemsgqueue(&q);
			break;
		case ARDUINO_TURNOFF_MSG:
			break;
		case ARDUINO_DELAY_MSG:
			arduino_setdelay(&executiondelay);
			break;
		default:
			errorblink_cmd();
			DBG(Serial.println("Internal command not recognized"););
			break;
	}

}

#endif
