
BlinkM Example Code   
===================
-- 20080610 -- Tod E. Kurt, http://thingm.com/

This zip file "BlinkM_Examples.zip" available from http://blinkm.thingm.com 
contains several examples of how to talk with BlinkMs.  It will be updated 
periodically as new examples are created.  If you have an interesting example
you would like added to this example set, contact me.

Note: in general, the examples will reset the I2C address of the BlinkM 
      to 0x10.  If you don't want this behavior, comment out the 
      "BlinkM_setAddress()" line in setup() and change the variable 
      "blinkm_addr" to match your BlinkM's address.


Arduino Examples
----------------
- BlinkMCommunicator 
  - A simple serial-to-i2c gateway for PC controlling of BlinkM, 
    like via Processing or BlinkM Sequencer

- BlinkMTester
  - A general tool to play with a single BlinkM

- BlinkMMulti
  - A simple example showing how to communicate with multiple BlinkMs

- BlinkMScriptWriter
  - A demonstration of how to write BlinkM light scripts with Arduino

- BlinkMScriptWriter2
  - Another demonstration of how to write BlinkM light scripts with Arduino

- BlinkMChuck
  - Control the hue & brightness of a BlinkM with a Wii Nunchuck

- BlinkMCylon
  - Control a bus of 13 BlinkMs to make a multi-colored Cylon-like display


Processing Examples
-------------------
- BlinkMSequencer
  - The drum machine-like application to program a BlinkM from Mac or Windows


Basic Stamp Examples
--------------------
- BlinkMTest.bs2  (in "other-examples/BasicStamp")


Max/MSP Examples
----------------
- Bleything.patch  (in "other-examples/MaxMSP")
  From Ben Bleything, see:
     http://blog.bleything.net/2008/3/23/controlling-a-blinkm-from-max-msp


Other
-----
- blinkm_nonvol_data.h
  - The contents of the BlinkM ROM light scripts



history
-------
- 20080120 - initial version
- 20080125 - included BlinkMChuck that was left out
- 20080203 - updated BlinkM_funcs.h & BlinkMChuck, improved BlinkMMulti
- 20080610 - fixed BlinkMScriptWriter, added BlinkMScriptWriter2
- 20080615 - added other-examples

