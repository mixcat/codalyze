# Status #
  * I2C Slave is working, receiving and sending. Used Don Blake's code, from AvrFreaks.
  * I2C Master is working, by adding pullup resistors. Sometimes it goes in error, adding some recovery code fixes it. Still, it's weird. ~~At least for one minute. Then goes amok. compiles but locks on transmit while waiting for SCL pin to get high. Might be because the bus is not being pulled up~~.

> I2C is not the only possible solution for communication. 1-Wire looks promising: it is much slower, but can go further and would leave a free pin (with I2C we have no free pins):
http://www.embedded.com/9900637?_requestid=165743, http://www.embedded.com/9900637?_requestid=165743

# Introduction #

Collecting all useful references to end up having a BlinkM behave as master or slave on the bus


# Details #

## An email from Tod, BlinkM creator ##
Hi Matteo,

I2C is an asymmetric protocol: a typical bus has one I2C master and one or more I2C slave. The Arduino Wire library is how to do an I2C master. You need code on how to make an I2C slave.

Atmel provides a pretty good application note in "AVR312: Using the USI module as a I2C slave". Scroll down to download it here:
http://atmel.com/dyn/products/app_notes.asp?family_id=607
It's interesting if you want to know some of the theory behind how it works.

For more immediate results, you can find examples of people using this application note
with avr-gcc on the net. One of the best examples is d.tools, http://hci.stanford.edu/research/dtools/ and you can look at the source code to many different kinds of I2C
slaves they've implemented here:
http://d-tools.cvs.sourceforge.net/d-tools/dtools-avr-code/attiny45-slaves/

I think the d.tools examples are using the IAR compiler instead of the AVR-GCC compiler. There's no real difference, just slight syntax changes for a few things. To see an avr-gcc examples, poke around on avrfreaks.net. There's a few projects and forum posts that port AVR312 appnote. For example, here's one: http://www.avrfreaks.net/index.php?module=Freaks%20Academy&func=viewItem&item_id=425&item_type=project

... other email

- Timing. You need to have some sort of external sync pulse to keep BlinkMs synchronized.  They would need big external crystals to have better timing accuracy than they have now.  One way of creating an external sync pulse is to toggle all the BlinkM's reset pin at once. Similarly, toggle all their power lines.

- Reliance:  Send me any bad BlinkMs and I'll mail you replacements. Apologies that you got bad ones.  We test each one before it ships, but once in a while one fails afterwards.

- Distance:  Are you using external pullup resistors on SDA & SCL for  your master?  That will help with distance.  You can also clock the I2C master slower, I can give you some tips on how to do that if the pullup resistors don't work.  I am very curious to hear how the NXP I2C buffer chips work out if you end up using them.

- Firmware:  We can't release the BlinkM firmware source code due to some licensing agreements we signed with Philips/NXP.  However, from my "smart led prototypes" and the I2C slave code I mention in the GetSatisfaction post, you have pretty much all the component parts of  BlinkM code.

## A response to a question posted on arduino.cc forum ##

http://www.arduino.cc/cgi-bin/yabb2/YaBB.pl?num=1217539796/0

Hi Matteo,

wire.c is coded for use with a micro that has the TWI engine in it - which the ATMega8m mega16 and most other larger devices have.

In the ATtiny45 you have to use the USI (Universal Serial Interface) in TWI mode, so basically the code wont work. I done this, based on code that I got form another user on AVRfreaks http://avrfreaks.net .

> I have posted the zip file at: http://www.siliconrailway.com/resources/ATtiny45 i2c.zip . In the zip file you will find code that should support TWI/I2C on any AVR that has a USI peripheral.

On the Atmel website you will also find two application notes, AVR310 and AVR312, which have source code in them as well.

Blakes work is, if I recall, based on those app notes and I have successfully used Blakes code in a number of applications.

Good luck!

John
http://siliconrailway.com home of LEDuino

## Blinkm Pins ##

## Blinkm as TTiny45 + 3leds ##
Pins as reported on blinkm datasheet:

  * PB0: I2C-SDA / MOSI
  * **PB1**: **LED3** / MISO
  * PB2: I2C-SCK
  * **PB3**: **LED2**
  * **PB4**: **LED1**
  * PB5: RESET

A note says "NOTE: Be sure to burn fuses to select internal oscillator!"
"Burning fuses" is obscure...

DN020 - All AVR devices contain two Lock bits named LB1 and LB2. Programming these (“0”)
will add protection to the contents written to Flash and EEPROM memories according to
Table 1 below. The level of protection is divided in three modes, where mode 1 offers no
protection and mode 3 offers maximum protection. It is possible to move to a higher
mode of protection simply by reprogramming the Lock bits. The AVR allows changing
"high" bits to "low", but not the other way around. It is not possible to change a "low"
Lock bit to a "high", thus lowering the level of protection is not possible. To clear the
Lock bits, a complete Chip Erase is required, which erase the Flash memory.


## Links ##
  * [Introduction to I2C](http://www.embedded.com/story/OEG20010718S0073) Some interesting bits here: In a bind, an I2C slave can hold off the master in the middle of a transaction using what's called clock stretching (the slave keeps SCL pulled low until it's ready to continue). Most I2C slave devices don't use this feature, but every master should support it.
  * [A Quickstart Tutorial for ATMEL AVR Microcontrollers](http://imakeprojects.com/Projects/avr-tutorial/)
  * [AVR television](http://www.avrtv.com/)
  * [AVR Fuses HOWTO Guide](http://electrons.psychogenic.com/modules/arms/art/14/AVRFusesHOWTOGuide.php)
  * [DN020 - Understanding AVR Fuses and Lock bits](http://www.avrfreaks.net/index.php?func=viewItem&item_id=301&module=Freaks%20Tools)
  * [Programming AVR fuse bits – oscillator settings](http://www.scienceprog.com/programming-avr-fuse-bits-oscillator-settings/)
  * [AVR Fuse Calculator](http://palmavr.sourceforge.net/cgi-bin/fc.cgi?P_PREV=&P=ATtiny45)
  * [avrtutor: avr tutorial](http://www.avrtutor.com/tutorial/thermo/welcome.php)
  * [AVR-GCC Programming Guide](http://electrons.psychogenic.com/modules/arms/art/3/AVRGCCProgrammingGuide.php)
  * [home of another I2C library, possibly better than application note](http://homepage.hispeed.ch/peterfleury/avr-software.html)