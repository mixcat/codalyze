/*
 * BlinkMFastComm -- Communication gateway between Blinkms on i2c bus
 *
 * Command format is:
 * <i2c_addr><cmd_byte>[<send_byte0>...]
 * 
 * 2008, Matteo Caprari
 */
 
#ifndef _BLINKMFASTCOMM2
#define _BLINKMFASTCOMM2

#include "Wire.h"
#include "BlinkM_funcs.h"
int ledPin = 13;

void setup() {
  pinMode(ledPin, OUTPUT);
  initBlinkm(1);
  Serial.begin(19200); 
  Serial.println("BlinkMCommander ready");
}

#define POS_ADDR 0
#define POS_CMD 1

int pos = 0; // keeps track of relative position in a command. 0:address 1command 2<pos<=len+1:payload
int len = -1; // legth of the payload of command being sent

void loop() {  

  
  for(int i=0; i<Serial.available(); i++) {
    switch(pos) {
    case POS_ADDR: 
      Wire.beginTransmission(Serial.read());
      break;
    case POS_CMD: // this is cmd, get len and transmit
      byte def = Serial.read();   
      Wire.send(def);    
      len = BlinkM_getCmdLen(def);
      break;
    default: 
      Wire.send(Serial.read());
    }

    if (len != -1 && pos == len+1) {
      Wire.endTransmission();
      len = -1;
      pos = 0;
    } 
    else {
      pos++;
    }
  }

}   

void initBlinkm(int powered) {
  if( powered ) {
    BlinkM_beginWithPower();
  } 
  else {
    BlinkM_begin();
  }
}

#define CMD_GO_TO_RGB 'n'
#define CMD_FADE_TO_RGB 'c'
#define CMD_FADE_TO_HSB 'h'
#define FADE_TO_RANDOM_RGB 'C'
#define FADE_TO_RANDOM_HSB 'H'
#define CMD_PLAY_SCRIPT 'p'
#define CMD_STOP_SCRIPT 'o'
#define CMD_SET_FADE_SPEED 'f'
#define CMD_SET_TIME_ADJUST 't'
#define CMD_GET_CURRENT_RGB 'g'
#define CMD_WRITE_SCRIPT_LINE 'W'
#define CMD_READ_SCRIPT_LINE 'R'
#define CMD_SET_SCRIPT_LR 'L'
#define CMD_SET_ADDRESS 'A'
#define CMD_GET_ADDRESS 'a'
#define GET_FIRMWARE_VERSION 'Z'
#define SET_STARTUP_PARAMS 'B'
int BlinkM_getCmdLen(byte def) {
  switch(def) {
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
  }
  return -1;
}




#endif
