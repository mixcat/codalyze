# CYZ\_RGB: An alternative firmware for [BlinkM](http://thingm.com/products/blinkm) #
## Version Beta 1 ##

BlinkM is a Smart LED produced by [ThingM](http://thingm.com):

> BlinkM is a “Smart LED”, a networkable and
> programmable full-color RGB LED for hobbyists,
> industrial designers, prototypers, and experimenters.

Just connect it to an I2C bus and start sending commands like "Show RGB Color" or "Fade to HSV Value" and so on and so forth (it does much more; see the human-readable [data sheet](http://thingm.com/fileadmin/thingm/downloads/BlinkM_datasheet.pdf)).

The original BlinkM firmware from ThingM is closed source due to licensing issues, so CYZ\_RGB was written to provide an open-source implementation which can be shared and extended to support additional features.

The latest release implements all documented features of the original firmware with the exception of the built in light scripts which are sacrificed to make room for the extended features of CYZ\_RGB.

In addition to the stock features of the BlinkM firmware, CYZ\_RGB implements a 16-bit PWM routine and a logarithmic dimming table to provide smoother fades and more accurate color mixing. The normal BlinkM firmware controls the LED duty cycle on a linear curve, but the perceived brightness of the light output does not correspond to a linear scale. There is little difference between #808080 and #ffffff on a standard BlinkM although the expectation would be for the former color to be roughly half as bright as the latter. CYZ\_RGB fixes this problem and fades are more linear.

Future work on CYZ\_RGB may include expanding the I2C master-mode operation, adding support for 1Wire instead of I2C, supporting external timing/clocking for script synchronization, or supporting more advanced color transitions and fade easing. Of course, help is always appreciated!

# Download #

If you decide to download and give it a try, you'll find a hex file that can be burned to your BlinkM, MaxM, or other similarly wired ATTiny44, ATTiny45, or ATTiny85 microcontroller. A binary including support for running the device in I2C master mode is not distributed in this release.

[Browse](http://code.google.com/p/codalyze/source/browse/#svn/cyz_rgb/trunk) the source code.

[Download](http://code.google.com/p/codalyze/downloads/detail?name=cyz_rgb-beta1-2010_06_28-binary.zip) the latest binary release (beta1).

[Subscribe](http://codalyze.blogspot.com/feeds/posts/default) to news feed to follow
developement.

Get the sources with svn: `svn checkout http://codalyze.googlecode.com/svn/cyz_rgb/trunk cyz_rgb`

# Install #

Use avrdude to flash the chip:
```
avrdude -pt45 -cavrispmkII -Pusb -b115200 -Uflash:w:slave.hex:a 
```


## Interested? I need help ##

I'd like to hear from you. If you have any patch to submit it will be welcome.

_Please not that my experience in C is very limited. And it approaches zero when it comes to microcontroller programming. So, if you are any good at it, please be patient and expect the worse... i'm a java programmer ;). When you stop feeling sick after reading the code, well, drop me a line_

## Implemented features ##

|command name|cmd char|cmd byte|# args|# ret vals|format|implemented|
|:-----------|:-------|:-------|:-----|:---------|:-----|:----------|
|**Go to RGB Color Now**|n       |0x6e    |3     |0         |{‘n’,R,G,B}|**yes**    |
|**Fade to RGB Color**|c       |0x63    |3     |0         |{‘c’,R,G,B}|**yes**    |
|Fade to HSB Color|h       |0x68    |3     |0         |{‘h’,H,S,B}|yes        |
|Fade to Random RGB Color|C       |0x43    |3     |0         |{‘C’,R,G,B}|yes        |
|Fade to Random HSB Color|H       |0x48    |3     |0         |{‘H’,H,S,B}|yes        |
|Play Light Script|p       |0x70    |3     |0         |{‘p’,n,r,p}|yes        |
|Stop Script |o       |0x6f    |0     |0         |{‘o’} |yes        |
|Set Fade Speed|f       |0x66    |1     |0         |{‘f’,f}|yes        |
|Set Time Adjust|t       |0x74    |1     |0         |{‘t’,t}|yes        |
|Get Current RGB Color|g       |0x67    |0     |3         |{‘g’} |yes        |
|Write Script Line|W       |0x57    |7     |0         |{‘W’,n,p,d,c,a1,a2,a3}|yes<sup>2</sup>|
|Read Script Line|R       |0x52    |2     |5         |{‘R’,n,p}|yes<sup>1</sup>|
|Set Script Length & Repeats|L       |0x4c    |3     |0         |{‘L’,n,l,r}|yes        |
|Set BlinkM Address|A       |0x41    |4     |0         |{‘A’,a...}|yes        |
|Get BlinkM Address|a       |0x61    |0     |1         |{‘a’} |yes        |
|Get BlinkM Firmware Version|Z       |0x5a    |0     |1         |{‘z’} |yes<sup>4</sup>|
|Set Startup Parameters|B       |0x42    |5     |0         |{‘B’,m,n,r,f,t}|yes        |
|Pre-deﬁned light scripts|yes <sup>5</sup>| | | | | |

<sup>x</sup> Only available in subversion head.

<sup>2</sup> Max 10 lines. Only valid commands are go and fade to RGB. Script number 'n' is ignored, can only write script 0

<sup>4</sup> current head version returns 0.1

<sup>5</sup> only script 1. no predenfined script 0.

## Goals for next release ##

  * Write a comprehensive test suite
  * Decouple cyz\_cmd from cyz\_rgb (done)
  * Decouple usiTwiSlave from cyz\_cmd
  * Reduce binary footprint (now 4072b of 4096 available) (now 3740b, but still too big)
  * Add ability to write 2 scripts

Matteo Caprari <matteo.caprari gmail.com>