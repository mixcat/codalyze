#include <avr/io.h>
#include <util/twi.h>
#include <ctype.h>
#include <stdlib.h>
#include <stdio.h>
#include <inttypes.h>
#include <util/delay.h>
#include <avr/interrupt.h>
#include <avr/interrupt.h>
#include <avr/wdt.h>
#include <avr/pgmspace.h>
#include <stdlib.h>
//#include <avr/iom8.h>
#include "USI_TWI_Master.h"


#define PINRED PB3
#define PINGRN PB4
#define PINBLU PB1

#define RED_LED_ON PORTB |= 1<<PINRED
#define GRN_LED_ON PORTB |= 1<<PINGRN
#define BLU_LED_ON PORTB |= 1<<PINBLU

#define RED_LED_OFF PORTB &= ~(1<<PINRED)
#define GRN_LED_OFF PORTB &= ~(1<<PINGRN)
#define BLU_LED_OFF PORTB &= ~(1<<PINBLU)


typedef struct _color {
    unsigned char r;
	unsigned char g;
	unsigned char b;
} Color;

//unsigned int ticks = 1;
// current color
Color color;
unsigned char slaveAddress;
int main(void)
{
	// put pins connected to leds in output mode (DDRB is Data Direction Register B)
	DDRB |= 1<<PINRED;
	DDRB |= 1<<PINGRN;
	DDRB |= 1<<PINBLU;

	TIFR   = (1 << TOV0);	// clear interrupt flag
  	TIMSK  = (1 << TOIE0);	// enable overflow interrupt
	TCCR0B = (1 << CS00);	// start timer, no prescale
	TIMSK  |= _BV(TOIE0);
	USI_TWI_Master_Initialise();
	sei(); // enable interrupts

	color.r = 127;
	color.g = 255;
	color.b = 0;

	slaveAddress = 0x26;		// This can be change to your own address

	//usiTwiSlaveInit(slaveAddress);
	//int cnt = 0;
	//char rcv[4];
	for(;;)
	{
	}

	    // Do something else while waiting for the TWI transceiver to complete.

	   // asm volatile ("NOP" ::);


	return 1;
}


/***
 *	Triggered when timer overflows.
 *	Every color is displayed for a number of firings that equals its RGB value, and then turned off.
 *	Extra care should be taken on green, which is more visible to the eye.
 *
 *	Each pin/led can only turned on or off. By mixing 3 colors we obtain only 8 combinations.
 *	To overcome this limitation, instead of limiting the output brightness, every led is turned on
 *  for a slice of time proportional to their RGB value.
 *
 *	If color is {255,127,0}, on a cycle of 255 ticks,
 *	white will always be on, green only 127 times, blue always off.
 *
 *  Possibly, the biggest value in color should be stretched to 255 and the other scaled,
 *	otherwise we get no light on the upper end of the tick,
 *  risking flickering. Does this stand for color rendering?
 *
 *	Using SIGNAL we cannot be interrupted here. tbc?
 */
#define TWI_GEN_CALL         0x00
ISR(SIG_OVERFLOW0)
{
	static unsigned int sigcount = 0;
	static unsigned char count = 0xFF;

	// Every time softcount overflows, turn all lights on
	if (++count == 0) {
		RED_LED_ON;
		GRN_LED_ON;
		BLU_LED_ON;
	}

	// when the target value for a color is reached, turn it off
	if (count == color.r) RED_LED_OFF;
	if (count == color.g) GRN_LED_OFF;
	if (count == color.b) BLU_LED_OFF;

	if (++sigcount == 0) { //this happens every about 3 secs, TODO: learn to calc how long betwwen each overflow
		color.r -= 127;
		color.g -= 127;
		color.b -= 127;
		unsigned char messageBuf[5];
		// send new color to slave
			messageBuf[0] = TWI_GEN_CALL;     // The first byte must always consit of General Call code or the TWI slave address.
		    messageBuf[1] = 'c';             // The command or data to be included in the general call.
		    messageBuf[2] = color.r;
		    messageBuf[3] = color.g;
		    messageBuf[4] = color.b;
		    unsigned char rt = USI_TWI_Start_Transceiver_With_Data( messageBuf, 5 );
		    if (!rt) {
		    	USI_TWI_Master_Initialise();
		    	color.r = 0;
		    	color.g = 0;
		    	color.b = 0;
		    }
		    //USI_TWI_Get_State_Info();
		    //USI_TWI_Master_Stop();



		  //__enable_interrupt();


	}
}
