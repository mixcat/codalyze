
/***************************************************************************
*
* Dan Gates
*
* File              : I2C_Slave.c
* Compiler          : AVRstudio 4
* Revision          : 1.0
* Date              : Tuesday, June 11, 2007
* Revised by        : Dan Gates
*
*
* Target device		: ATtiny45
*
* AppNote           : AVR312 - Using the USI module as a I2C slave.
*
* Description       : Program for returning Analog data over an I2C port.
*                   : This program assumes that you have an analog sensor
*					: attached to PB4 (ADC2). Other inputs are digital and
*					: were used to set the slave address (not used in this sample).
*
* Connections
*
*                             AT tiny 45
*                 +--------------------------------+
* Address select1 | 1 pb5 reset              VCC 8 |
* Address select2 | 2 pb3                SCL pb2 7 | SCL
*  In from analog | 3 pb4 ADC2               pb1 6 | Address select3
*                 | 4 GND                SDA pb0 5 | SDA
*                 +--------------------------------+
****************************************************************************/

#include <avr/io.h>
#include <avr/interrupt.h>
#include <avr/pgmspace.h>
#include "usiTwiSlave.h"

#define sbi(sfr, bit) (_SFR_BYTE(sfr) |= _BV(bit))
#define cbi(sfr, bit) (_SFR_BYTE(sfr) &= ~_BV(bit))

uint16_t ADCIN(void);
uint16_t value;
uint16_t analog;


/* Brief: This section determines what format to return the value in. */

enum { DATA1, DATA2, RAW };

//-----------------------------------------------------------------------------

/* This section allows you to select between three different modes. In this sample
each return the value of the analog at different levels just to give an idea of how
it can be used. */

void mode(uint8_t mode, uint8_t on)
{
  analog = ADCIN();
  switch (mode)
  {
    case DATA1: if (on) (value = analog * 2); break;
    case DATA2: if (on) (value = analog / 10); break;
    case RAW:  	if (on) (value = analog); break;
  }
}

/* Brief: The main function.
 * The program entry point. Initates TWI and enters eternal loop, waiting for data.
 */
int main(void)
{
  unsigned char slaveAddress, temp;

  sei();

  ADCSRA = _BV(ADEN)       // enable ADC doo-dad
        | _BV(ADSC)        // start first conversion (freerunning)
        | _BV(ADPS2)       // enable ADC interrupt with 2 prescaler
		| _BV(ADPS1);

    while ( ADCSRA & ( 1 << ADSC ) );    // wait for complete conversion

	cbi(DDRB, DDB1);        // pb1 set up as input
 	cbi(DDRB, DDB3);        // pb3 set up as input
//   cbi(DDRB, DDB5);		// pb5 set up as input (use only in production, wipes out reset pin)
	sbi(PORTB, PB1);		// Set pb1 internal pullup
	sbi(PORTB, PB3);		// Set pb3 internal pullup
//	 sbi(PORTB, PB5);		// (use only in production, wipes out reset pin)

  slaveAddress = 0x26;		// This can be change to your own address


  usiTwiSlaveInit(slaveAddress);

  // This loop runs forever. If the TWI Transceiver is busy the execution will
  // just continue doing other operations.
  for(;;)
  {


    if(usiTwiDataInReceiveBuffer())
    {
	 temp = usiTwiReceiveByte();
      switch (temp)
       {
	   case 1: mode(DATA1, 1);   break;		// the case is selected by a single
       case 2: mode(DATA2, 1);   break;		// digit in the master code. (1,2 or 3)
       case 3: mode(RAW, 1);     break;
       }

     usiTwiTransmitByte(value);
    }

    // Do something else while waiting for the TWI transceiver to complete.

    asm volatile ("NOP" ::);
  }
}

/*
 *  ADCIN
 *
 *  Read the specified Analog to Digital Conversion channel
 *
 */

uint16_t ADCIN(void)
{

  ADMUX = _BV(ADLAR)     // left justify result
        | _BV(REFS1)     // use internal 2.56 volt reference with
        | _BV(REFS2)     // no external cap
        | _BV(MUX1);     // use ADC number 2
  ADCSRA = _BV(ADEN)     // enable ADC doo-dad
        | _BV(ADSC);     // start first conversion (freerunning)
    asm volatile ("NOP" ::);
    asm volatile ("NOP" ::);

    /* wait for complete conversion and return the result */
    while ( ADCSRA & ( 1 << ADSC ) );

    return ADCH;
}
