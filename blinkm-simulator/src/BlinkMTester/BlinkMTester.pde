/*
 * BlinkMTester -- Simple command-line tool to play with BlinkMs
 *
 *
 * BlinkM connections to Arduino
 * PWR - -- gnd -- black -- Gnd
 * PWR + -- +5V -- red   -- 5V
 * I2C d -- SDA -- green -- Analog In 4
 * I2C c -- SCK -- blue  -- Analog In 5
 *
 * Note: This sketch resets the I2C address of the BlinkM.
 *       If you don't want this behavior, comment out "BlinkM_setAddress()"
 *       in setup() and change the variable "blink_addr" to your BlinkM's addr.
 *
 * 2007, Tod E. Kurt, ThingM, http://thingm.com/
 *
 */


#include "Wire.h"
#include "BlinkM_funcs.h"

#define BLINKM_ARDUINO_POWERED 1

byte blinkm_addr = 0x09; // the address we're going to set the BlinkM to

char serInStr[30];  // array that will hold the serial input string


void help()
{
  Serial.println("\nBlinkMTester!\n"
                 "'c<rrggbb>' to fade to an rgb color\n"
                 "'h<hhssbb>' to fade to an hsb color\n"
                 "'C<rrggbb>' to fade to a random rgb color\n"
                 "'H<hhssbb>' to fade to a random hsb color\n"
                 "'p<n>' to play a script\n"
                 "'o' to stop a script\n"
                 "'f<nn>' to change fade speed\n"
                 "'t<nn>' to set time adj\n"
                 "'g' to get current color\n"
                 "'A<n>' set I2C address\n"
                 "'a' get I2C address\n"
                 "'Z' get BlinkM version\n"
                 );
}

void setup()
{
    if( BLINKM_ARDUINO_POWERED ) {
        BlinkM_beginWithPower();
    } else {
        BlinkM_begin();
    }
    BlinkM_setAddress( blinkm_addr );  // comment out to not set address
    
    Serial.begin(19200);
    byte rc = BlinkM_checkAddress( blinkm_addr );
    if( rc == -1 ) 
        Serial.println("\r\nno response");
    else if( rc == 1 ) 
        Serial.println("\r\naddr mismatch");

    help();
    Serial.print("cmd>");
}

void loop()
{
    int num;
    //read the serial port and create a string out of what you read
    if( readSerialString() ) {
        Serial.println(serInStr);
        char cmd = serInStr[0];  // first char is command
        char* str = serInStr;
        while( *++str == ' ' );  // got past any intervening whitespace
        num = atoi(str);         // the rest is arguments (maybe)
        if( cmd == '?' ) {
            help();
        }
        else if( cmd == 'c' || cmd=='h' || cmd == 'C' || cmd == 'H' ) {
            byte a = toHex( str[0],str[1] );
            byte b = toHex( str[2],str[3] );
            byte c = toHex( str[4],str[5] );
            if( cmd == 'c' ) {
                Serial.print("Fade to r,g,b:");
                BlinkM_fadeToRGB( blinkm_addr, a,b,c);
            } else if( cmd == 'h' ) {
                Serial.print("Fade to h,s,b:");
                BlinkM_fadeToHSB( blinkm_addr, a,b,c);
            } else if( cmd == 'C' ) {
                Serial.print("Random by r,g,b:");
                BlinkM_fadeToRandomRGB( blinkm_addr, a,b,c);
            } else if( cmd == 'H' ) {
                Serial.print("Random by h,s,b:");
                BlinkM_fadeToRandomHSB( blinkm_addr, a,b,c);
            }
            Serial.print(a,HEX); Serial.print(",");
            Serial.print(b,HEX); Serial.print(",");
            Serial.print(c,HEX); Serial.println();
        }
        else if( cmd == 'f' ) {
            Serial.print("Set fade speed to:"); Serial.println(num,DEC);
            BlinkM_setFadeSpeed( blinkm_addr, num);
        }
        else if( cmd == 't' ) {
            Serial.print("Set time adj:"); Serial.println(num,DEC);
            BlinkM_setTimeAdj( blinkm_addr, num);
        }
        else if( cmd == 'p' ) {
            Serial.print("Play script #");
            Serial.println(num,DEC);
            BlinkM_playScript( blinkm_addr, num,0,0 );
        }
        else if( cmd == 'o' ) {
            Serial.println("Stop script");
            BlinkM_stopScript( blinkm_addr );
        }
        else if( cmd == 'g' ) {
            Serial.print("Current color: ");
            byte r,g,b;
            BlinkM_getRGBColor( blinkm_addr, &r,&g,&b);
            Serial.print("r,g,b:"); Serial.print(r,HEX);
            Serial.print(",");      Serial.print(g,HEX);
            Serial.print(",");      Serial.println(b,HEX);
        }
        /*
        else if( cmd == 'W' ) { 
              Serial.println("Writing new eeprom script");
              for(int i=0; i<6; i++) {
              blinkm_script_line l = script_lines[i];
              BlinkM_writeScriptLine( blinkm_addr, 0, i, l.dur,
              l.cmd[0],l.cmd[1],l.cmd[2],l.cmd[3]);
              }
        }
        */
        else if( cmd == 'A' ) {
            if( num>0 && num<0xff ) {
                Serial.print("Setting address to: ");
                Serial.println(num,DEC);
                BlinkM_setAddress(num);
                blinkm_addr = num;
            }
        }
        else if( cmd == 'a' ) {
            Serial.print("Address: ");
            num = BlinkM_getAddress(blinkm_addr); // zero means 'general call'
            Serial.println(num);
        }
        else if( cmd == 'Z' ) { 
            Serial.print("BlinkM version: ");
            num = BlinkM_getVersion(blinkm_addr);
            if( num == -1 )
                Serial.println("couldn't get version");
            Serial.print( (char)(num>>8), BYTE ); 
            Serial.println( (char)(num&0xff),BYTE );
        }
        else if( cmd == 'B' ) {
            Serial.print("Set startup mode:"); Serial.println(num,DEC);
            BlinkM_setStartupParams( blinkm_addr, num, 0,0,1,0);
        }
        else { 
            Serial.println("Unrecognized cmd");
        }
        serInStr[0] = 0;  // say we've used the string
        Serial.print("cmd>");
    } //if( readSerialString() )
  
}

// a really cheap strtol(s,NULL,16)
#include <ctype.h>
uint8_t toHex(char hi, char lo)
{
    uint8_t b;
    hi = toupper(hi);
    if( isxdigit(hi) ) {
        if( hi > '9' ) hi -= 7;      // software offset for A-F
        hi -= 0x30;                  // subtract ASCII offset
        b = hi<<4;
        lo = toupper(lo);
        if( isxdigit(lo) ) {
            if( lo > '9' ) lo -= 7;  // software offset for A-F
            lo -= 0x30;              // subtract ASCII offset
            b = b + lo;
            return b;
        } // else error
    }  // else error
    return 0;
}

//read a string from the serial and store it in an array
//you must supply the array variable
uint8_t readSerialString()
{
    if(!Serial.available()) {
        return 0;
    }
    delay(10);  // wait a little for serial data
    int i = 0;
    while (Serial.available()) {
        serInStr[i] = Serial.read();   // FIXME: doesn't check buffer overrun
        i++;
    }
    serInStr[i] = 0;  // indicate end of read string
    return i;  // return number of chars read
}


