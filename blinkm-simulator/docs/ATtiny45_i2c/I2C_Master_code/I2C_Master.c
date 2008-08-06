//

/*
                                                            
                   +--------------------------------+
                   |  1 pc6 reset   adc5/SCL pc5 28 |
                   |  2 pd0 rxd     adc4/SDA pc4 27 |
                   |  3 pd1 txd         adc3 pc3 26 |
                   |  4 pd2 INT0        adc2 pc2 25 |
                   |  5 pd3 INT1        adc1 pc1 24 |
                   |  6 pd4 XCK/T0      adc0 pc0 23 |
                   |  7 VCC                  GND 22 |
                   |  8 GND                 AREF 21 |
                   |  9 pb6 XTAL1           AVCC 20 |
                   | 10 pb7 XTAL2        SCK pb5 19 |
                   | 11 pd5 T1          MISO pb4 18 |
                   | 12 pd6 AIN0    MOSI/OC2 pb3 17 |
                   | 13 pd7 AIN1    !SS/OC1B pb2 16 |
                   | 14 pb0 ICP         OC1A pb1 15 |
                   +--------------------------------+
*/

/**************************************************************************
**  Include Files
***************************************************************************/
#include <stdlib.h>
#include <avr/io.h>
#include <stdio.h>
#include <inttypes.h>
#include <avr/pgmspace.h>
#include <util/twi.h>
#include "i2c.h"
#include "lcd.h"
#include "osccal.h"

/*************************************************************************
** special function definnitions
*************************************************************************/
#define sbi(sfr, bit) (_SFR_BYTE(sfr) |= _BV(bit))
#define cbi(sfr, bit) (_SFR_BYTE(sfr) &= ~_BV(bit))

void delay_ms( uint16_t milliseconds );
volatile uint16_t ms_count;
static int put_char(char c, FILE *stream);
static FILE mystdout = FDEV_SETUP_STREAM(put_char, NULL, _FDEV_SETUP_WRITE);

/*************************************************************************/
static int put_char(char c, FILE *stream)
{
   lcd_putc(c);
   return 0;
}

/*************************************************************************
** constant definitions
**************************************************************************/
static const PROGMEM unsigned char copyRightChar[] =
{
	0x07, 0x08, 0x13, 0x14, 0x14, 0x13, 0x08, 0x07,
	0x00, 0x10, 0x08, 0x08, 0x08, 0x08, 0x10, 0x00
};


/*************************************************************************
 delay loop for small accurate delays: 16-bit counter, 4 cycles/loop
*************************************************************************/
static inline void _delayFourCycles(unsigned int __count)
{
    if ( __count == 0 )
        __asm__ __volatile__( "rjmp 1f\n 1:" );    // 2 cycles
    else
        __asm__ __volatile__ (
    	    "1: sbiw %0,1" "\n\t"
    	    "brne 1b"                              // 4 cycles/loop
    	    : "=w" (__count)
    	    : "0" (__count)
    	   );
}

/*************************************************************************
delay = delay in <us> microseconds
The number of loops is calculated at compile-time from MCU clock frequency
*************************************************************************/
#define delay(us)  _delayFourCycles( ( ( 1*(XTAL/4000) )*us)/1000 )

/************************************************************************
delay_ms = delay in milliseconds
Code to create longer delays in the millisecond range rather than
microseconds.
*************************************************************************/
void delay_ms( uint16_t milliseconds )
{
    uint16_t i;

    for ( i = 0; i < milliseconds; ++i )
    {
        delay( 1000 );
    }
}

/*************************************************************************
This section is used to provide feedback durring tests. Error messages
are printed to the LCD
**************************************************************************/
  uint8_t data;

enum {ERROR0, ERROR1, ERROR2};

void error(uint8_t error, uint8_t on)
{
  switch (error)
  {
    case ERROR0: if (on) printf("No Error\n");		 else printf("No Error\n"); break;
    case ERROR1: if (on) printf("Improper ACK\n");	 else printf("No Error\n"); break;
    case ERROR2: if (on) printf("Timed Out\n");		 else printf("No Error\n"); break;
  }
}

/****************************************************************************
This section is the main entry point of the code
our program begins here
****************************************************************************/
int main (void)
{
  osccal();					// Calibrate our internal RC oscilator
  uint8_t address;			// Declare address as an 8bit variable
  int8_t returncode;		// Declare returncode as an 8bit variable

	
  /* Initialize the LCD */
    lcd_init(LCD_DISP_ON);
    stdout = &mystdout;    	// set the output stream
    lcd_clrscr();			// clear display and home cursor


  /* This area enables the TWI (I2C) interface and sets the communication speed */
  TWSR = 0;  
  TWBR = 32;  // <100khz with no prescaler at 8mhz
//  TWCR = _BV(TWEN);      // enable that sucker
//     | _BV(TWEA)       // twi enable acknowledge bit
//     | _BV(TTWSTA)     // start condition bit - masters use it
//     | _BV(TTWSTO)     // stop condition bit - masters use it
//     | _BV(TWWC)       // write collision flag - we don't set
//     | _BV(TWIE);      // twi interrupt enable 
//       _BV(TWINT)      // TWI interrupt flag - I don't set it
 while(1)
  {
  /* Set the address of the slave device you wish to communicate with here */
  address = 0x26;
  
  /* This section starts the transpondance between the slave and Master */		 
  returncode = i2c_start(0x10);
  /* un-comment the following to check for errors, use only one section at a time */
 // switch(returncode)
 // {
 //   case 0: error(ERROR0, 1); break;
 //   case -1: error(ERROR1, 1); break;
 //   case -2: error(ERROR2, 1); break;
 // }
  returncode = i2c_sla_rw(address, 0, TW_MT_SLA_ACK);
  /* un-comment the following to check for errors, use only one section at a time */
//  switch(returncode)
//  {
//    case 0: error(ERROR0, 1); break;
//    case -1: error(ERROR1, 1); break;
//    case -2: error(ERROR2, 1); break;
//  }

/* selecting a 1, 2 or 3 here will change the case in the slave device and return 
   a different value. */
  returncode = i2c_data_tx(2, TW_MT_DATA_ACK);
  /* un-comment the following to check for errors, use only one section at a time */
//  switch(returncode)
//  {
//    case 0: error(ERROR0, 1); break;
//    case -1: error(ERROR1, 1); break;
//    case -2: error(ERROR2, 1); break;
//  }

  returncode = i2c_start(0x10);
  /* un-comment the following to check for errors, use only one section at a time */
//  switch(returncode)
//  {
//    case 0: error(ERROR0, 1); break;
//    case -1: error(ERROR1, 1); break;
//    case -2: error(ERROR2, 1); break;
//  }

  returncode = i2c_sla_rw(address, 1, TW_MR_SLA_ACK);
  /* un-comment the following to check for errors, use only one section at a time */
// switch(returncode)
// {
//	  case 0: error(ERROR0, 1); break;
//    case -1: error(ERROR1, 1); break;
//    case -2: error(ERROR2, 1); break;
//  }
  data = i2c_readNak();
  i2c_stop();
  printf("Distance = %i\n",data);

  delay_ms(50);

  lcd_clrscr();			// clear display and home cursor
  }

}
