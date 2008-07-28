#ifndef _DEFINES_H
#define _DEFINES_H

#define DBG(x)


#define BLINKM_INIT_POWERED true
static const int recordingPin = 13;

#define ARDUINO_MSG 128



#define ARDUINO_RECORD_MSG 'r'
#define ARDUINO_PLAYBACK_MSG 'p'
#define ARDUINO_ERASE_MSG 'f'
#define ARDUINO_TURNOFF_MSG 'x'
#define ARDUINO_DELAY_MSG 'P'



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
#define CMD_SLEEP  'P'


#endif
