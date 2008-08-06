#include <util/twi.h>
#include <avr/pgmspace.h>

#include <stdio.h>
#include <inttypes.h>

#include "i2c.h"

const char s_i2c_start_error[]   PROGMEM = "I2C START CONDITION ERROR";
const char s_i2c_sla_w_error[]   PROGMEM = "I2C SLAVE ADDRESS ERROR";
const char s_i2c_data_tx_error[] PROGMEM = "I2C DATA TX ERROR";
const char s_i2c_data_rx_error[] PROGMEM = "I2C DATA RX ERROR";
const char s_i2c_timeout[]       PROGMEM = "I2C TIMEOUT";
const char s_i2c_error[]         PROGMEM = "I2C ERROR\n";
const char s_fmt_i2c_error[]     PROGMEM = " TWCR=%02x STATUS=%02x\n";

extern volatile uint16_t ms_count;

//-----------------------------------------------------------------------------
int8_t i2c_stop(void)
/*
 * signal the end of an I2C bus transfer
 */
{
  TWCR = _BV(TWINT)|_BV(TWEN)|_BV(TWSTO);
  while (TWCR & _BV(TWSTO))
    ;
  return 0;
}


//-----------------------------------------------------------------------------
void i2c_error(const char * message, uint8_t cr, uint8_t status)
/*
 * display the I2C status and error message and release the I2C bus
 */
{
  i2c_stop();
  //printf_P(message);
  //printf_P(s_fmt_i2c_error, cr, status);
}


//-----------------------------------------------------------------------------
int8_t i2c_start(uint8_t expected_status)
/*
 * signal an I2C start condition in preparation for an I2C bus
 * transfer sequence (polled)
 */
{
  uint8_t status;

  ms_count = 0;
  /* send start condition to take control of the bus */
  TWCR = I2C_START;
  while (!(TWCR & _BV(TWINT)) && (ms_count++ < I2C_TIMEOUT)) ;
  if (ms_count >= I2C_TIMEOUT)
    return -1;
  /* verify start condition */
  status = TWSR;
  if (status != expected_status)
    return -2;
  return 0;
}


//-----------------------------------------------------------------------------
int8_t i2c_sla_rw(uint8_t device, uint8_t op, uint8_t expected_status)
/*
 * initiate a slave read or write I2C operation (polled)
 */
{
  uint8_t sla_w;
  uint8_t status;

  ms_count = 0;
  sla_w = (device << 1) | op;
  TWDR = sla_w;
  TWCR = I2C_MASTER_TX;
  while (!(TWCR & _BV(TWINT)) && (ms_count++ < I2C_TIMEOUT)) ;
  if (ms_count >= I2C_TIMEOUT)
    return -1;
  status = TWSR;
  if ((status & 0xf8) != expected_status)
    return -2;
  return 0;
}


//-----------------------------------------------------------------------------
int8_t i2c_data_tx(uint8_t data, uint8_t expected_status)
/*
 * transmit a data byte onto the I2C bus (polled)
 */
{
  uint8_t status;

  ms_count = 0;
  TWDR = data;
  TWCR = I2C_MASTER_TX;
  while (!(TWCR & _BV(TWINT)) && (ms_count++ < I2C_TIMEOUT)) ;
  if (ms_count >= I2C_TIMEOUT)
    return -1;
  status = TWSR;
  if ((status & 0xf8) != expected_status)
    return -2;
  return 0;
}


//-----------------------------------------------------------------------------
int8_t i2c_data_rx(uint8_t *data, uint8_t ack, uint8_t expected_status)
/*
 * receive a data byte from the I2C bus (polled)
 */
{
  uint8_t status;

  ms_count = 0;
  if (ack) TWCR = _BV(TWINT)|_BV(TWEN)|_BV(TWEA);
  else TWCR = _BV(TWINT)|_BV(TWEN);
  while (!(TWCR & _BV(TWINT)) && (ms_count < I2C_TIMEOUT)) ;

  if (ms_count >= I2C_TIMEOUT)
    return -1;
  status = TWSR;
  if ((status & 0xf8) != expected_status)
    return -1;
  *data = TWDR;
  return 0;
}



/*************************************************************************
 Read one byte from the I2C device, read is followed by a stop condition

 Return:  byte read from I2C device
*************************************************************************/
unsigned char i2c_readNak(void)
{
	TWCR = (1<<TWINT) | (1<<TWEN);
	while(!(TWCR & (1<<TWINT)));

    return TWDR;

}/* i2c_readNak */
