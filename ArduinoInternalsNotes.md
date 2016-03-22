# Introduction #

Random notes about what's going on.

# Details #

Apparently the Wire library is in arduino-0011/hardware/libraries/Wire (where arduino-0011 is the package). It's a c++ library, with library calls etc. Perhaps they can be avoided in order to save memory space.

Also, in the hacking/ part of the website, they talk about avoiding the boot program for the same reason. More investigation required. At least we now know where the famous Wire.h is, and maybe can go from an app of 7k down to a few kb.
Maybe.